package com.example.zuul.config.filter;
import com.alibaba.fastjson.JSON;
import com.example.zuul.util.StringUtil;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class ErrorZuulFilter extends SendErrorFilter {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger("ZUUL_BI");

    private final String tab = "    ";

    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletRequest request = context.getRequest();
            //获取所有参数
            Map<String, String[]> queryParams = request.getParameterMap();
            //获取返回数据
            InputStream dataStream = context.getResponseDataStream();
            String result = "";
            if (dataStream != null) {
                result = new BufferedReader(new InputStreamReader(dataStream)).lines().collect(Collectors.joining(System.lineSeparator()));
                context.setResponseBody(result);
            }
            //获取异常数据
            ZuulException e = this.findZuulExceptions(context.getThrowable());
            String message = e.getMessage();
            String userName = request.getHeader("userName") == null ? "" : request.getHeader("userName");
            logger.error(StringUtil.appendStrs("userName:", userName, tab, "url:", request.getRequestURI(), tab, "params:", JSON.toJSONString(queryParams), tab, "result:", result), e);
            // Remove error code to prevent further error handling in follow up filters
            // 删除该异常信息,不然在下一个过滤器中还会被执行处理
            context.remove("throwable");
            request.setAttribute("javax.servlet.error.status_code", e.nStatusCode);
            request.setAttribute("javax.servlet.error.exception", e);
            request.setAttribute("javax.servlet.error.message", e.errorCause);
            context.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            context.set("error.exception", e);
        } catch (Exception var5) {
            ReflectionUtils.rethrowRuntimeException(var5);
        }
        return null;
    }

    ZuulException findZuulExceptions(Throwable throwable) {
        if (throwable.getCause() instanceof ZuulRuntimeException) {
            return (ZuulException) throwable.getCause().getCause();
        } else if (throwable.getCause() instanceof ZuulException) {
            return (ZuulException) throwable.getCause();
        } else {
            return throwable instanceof ZuulException ? (ZuulException) throwable : new ZuulException(throwable, 500, (String) null);
        }
    }
}