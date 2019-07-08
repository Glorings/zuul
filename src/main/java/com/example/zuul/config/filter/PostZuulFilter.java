package com.example.zuul.config.filter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.zuul.service.bo.BILoggerBo;
import com.example.zuul.util.RequestUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PostZuulFilter extends ZuulFilter {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger("ZUUL_BI");

    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String fourSpace = "    ";//这里是四个空格
    private final String tab = "    ";

    @Resource
    RequestUtil requestUtil;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
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
    	//获取所有参数
        String params = "";
        String realParams = "";
        try{
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletRequest request = context.getRequest();
            if (request.getContentType() != null && request.getContentType().contains("json")) {
                try {
                    params = new BufferedReader(new InputStreamReader(request.getInputStream())).lines().collect(Collectors.joining(System.lineSeparator()));
                    realParams= requestUtil.getRequestInfo(JSON.parseObject(params)).toJSONString() ;
                } catch (IOException e) {
                	logger.error("请求参数解析异常params="+params+",e:",e);
                }
            } else {
                Map<String, String[]> queryParams = request.getParameterMap();
                params = JSON.toJSONString(queryParams);
                realParams=params;
            }
            //获取返回数据
            InputStream dataStream = context.getResponseDataStream();
            String result = "";
            if (dataStream != null) {
                result = new BufferedReader(new InputStreamReader(dataStream)).lines().collect(Collectors.joining(System.lineSeparator()));
                context.setResponseBody(result);
            }
            BILoggerBo biLoggerBo = new BILoggerBo();
            biLoggerBo.setT(dateTimeFormat.format(new Date()));
            JSONObject headerObject=new JSONObject();
            Enumeration<String> headerNames= request.getHeaderNames();
            while ( headerNames.hasMoreElements()){
                String headerName= headerNames.nextElement();
                headerObject.put(headerName, request.getHeader(headerName));
            }
            biLoggerBo.setU(request.getRequestURI());

            biLoggerBo.setI(realParams);
            biLoggerBo.setO(result);
            biLoggerBo.setH(headerObject.toJSONString());

            if (context.getResponseStatusCode() == HttpServletResponse.SC_OK) {
                //成功保存请求日志
                logger.info(JSON.toJSONString(biLoggerBo));
            } else {
                //错误日志ErrorZuulFilter统一处理
                String defaultResult="{\"status\":"+context.getResponseStatusCode()+",\"details_message\":\""+context.getResponseStatusCode()+"\",\"message\":\"系统繁忙，请稍后\"}";
                logger.info(JSON.toJSONString(biLoggerBo));
                context.setResponseBody(defaultResult);
            }
        }catch (Exception ex){
            logger.info("request error，params="+params+",realParams="+realParams+",error:",ex);
        }

        return null;
    }
}
