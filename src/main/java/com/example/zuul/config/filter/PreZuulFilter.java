package com.example.zuul.config.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreZuulFilter extends ZuulFilter {

    @Value("${zuul.ignore.url}")
    private String authorIgnoreUrl;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
    /*    if (!request.getRequestURI().contains("/open/") && !authorIgnoreUrl.contains(request.getRequestURI())) {
            throw new ZuulException("提示:只开放带有open标识的url", 404, "");
        }*/
        return null;
    }
}