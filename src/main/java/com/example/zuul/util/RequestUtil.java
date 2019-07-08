package com.example.zuul.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import static com.example.zuul.util.DataTransferUtil.getParams;

@Component
@RefreshScope
public class RequestUtil {
    @Value("${spring.cloud.profile}")
    private String profile;
    @Value("${host.app.key}")
    private String hostAppKey;
    @Value("${aes.password}")
    private String aesPassword;

    /**
     * 通过appSecret解密获取参数
     *
     * @param appParams app 原始参数解析
     * @return
     */
    public JSONObject getRequestInfo(JSONObject appParams) {
        try {
            if(appParams == null){
                return new JSONObject();
            }
            //h5直连
            JSONObject test = appParams.getJSONObject("test");
            if (test != null && (profile.contains("local") || profile.contains("test"))) {
                appParams.put("appId", "aishangjie");
                return test;
            } else {
                JSONArray jsonArray = JSON.parseArray(AesUtil.decryptFromBase64(hostAppKey, aesPassword));
                String appSecret = "";
                String appId = appParams.getString("appId");
                String sign = appParams.getString("sign");
                String params = appParams.getString("params");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("appId").equals(appId)) {
                        appSecret = jsonObject.getString("appSecret");
                    }
                }
                if (appSecret.equals("")) {
                    return appParams;
                }
                JSONObject result = getParams(params, appSecret, sign);
                result.put("appId", appId);
                result.put("appSecret", appSecret);
                return result;
            }
        } catch (Exception ex) {
            return appParams;
        }
    }
}
