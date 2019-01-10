package com.ishanshan.gateway.filter;

import com.ishanshan.gateway.exception.GatewayException;
import com.ishanshan.gateway.exception.GatewayStatus;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_ERROR_FILTER_ORDER;

@Component
public class CustomSendErorFilter extends ZuulFilter {

    private static final Log log = LogFactory.getLog(SendErrorFilter.class);
    protected static final String SEND_ERROR_FILTER_RAN = "sendErrorFilter.ran";

    @Value("${error.path:/error}")
    private String errorPath;

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_ERROR_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // only forward to errorPath if it hasn't been forwarded to already
        return ctx.getThrowable() != null && !ctx.getBoolean(SEND_ERROR_FILTER_RAN, false);
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            ZuulException exception = findZuulException(ctx.getThrowable());
            HttpServletRequest request = ctx.getRequest();

            request.setAttribute("javax.servlet.error.status_code", exception.nStatusCode);

            log.warn("Error during filtering", exception);
            // 自定义异常
            if (exception.getCause() != null && exception.getCause() instanceof GatewayException) {
                request.setAttribute("javax.servlet.error.exception", exception.getCause());
            } else { // 系统异常
                String errorMessage = GatewayStatus.GATEWAY_ERROR.getMessage();
                if (StringUtils.hasText(exception.getMessage())) {
                    errorMessage = exception.getMessage();
                }
                GatewayException gatewayException =
                        new GatewayException(GatewayStatus.GATEWAY_ERROR.getCode(), errorMessage);
                request.setAttribute("javax.servlet.error.exception", gatewayException);
            }

            if (StringUtils.hasText(exception.errorCause)) {
                request.setAttribute("javax.servlet.error.message", exception.errorCause);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(this.errorPath);
            if (dispatcher != null) {
                ctx.set(SEND_ERROR_FILTER_RAN, true);
                if (!ctx.getResponse().isCommitted()) {
                    ctx.setResponseStatusCode(exception.nStatusCode);
                    dispatcher.forward(request, ctx.getResponse());
                }
            }
        } catch (Exception ex) {
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    ZuulException findZuulException(Throwable throwable) {
        if (throwable.getCause() instanceof ZuulRuntimeException) {
            // this was a failure initiated by one of the local filters
            return (ZuulException) throwable.getCause().getCause();
        }

        if (throwable.getCause() instanceof ZuulException) {
            // wrapped zuul exception
            return (ZuulException) throwable.getCause();
        }

        if (throwable instanceof ZuulException) {
            // exception thrown by zuul lifecycle
            return (ZuulException) throwable;
        }

        // fallback, should never get here
        return new ZuulException(throwable, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }
}
