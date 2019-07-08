/*
 *@Copyright (c) 2016,浙江阿拉丁电子商务股份有限公司 All Rights Reserved. 
 */
package com.example.zuul.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 *@类描述：
 *@author 何鑫 2017年1月18日  12:51:33
 *@注意：本内容仅限于浙江阿拉丁电子商务股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class DataTransferUtil {

    private static final LoggerUtil logger = new LoggerUtil(DataTransferUtil.class);

    /**
     * 通过appSecret解密获取参数
     * @param params
     * @param appSecret
     * @return
     */
    public static JSONObject getParamsWithOrder(String params, String appSecret, String sign) {
        //解密参数
        JSONObject result = paramsDecryptWithOrder(params, appSecret);
        //验证参数是否被修改
        String signCode = generateSign(result, appSecret).toUpperCase();
        if (signCode.equals(sign.toUpperCase())) {
            return result;
        }
        return null;
    }
    
    /**
     * 通过appSecret解密获取参数
     * @param params
     * @param appSecret
     * @return
     */
    public static JSONObject getParams(String params, String appSecret, String sign) {
        //解密参数
        JSONObject result = paramsDecrypt(params, appSecret);
        //验证参数是否被修改
        String signCode = generateSign(result, appSecret).toUpperCase();
        if (signCode.equals(sign.toUpperCase())) {
            return result;
        }
        return null;
    }

    /**
     * 通过appSecret加密参数
     *
     * @param params
     * @param appSecret
     * @return
     */
    public static String paramsEncrypt(JSONObject params, String appSecret) {
//        List<String> keys = new ArrayList<String>(params.keySet());
//        Collections.sort(keys);
//        JSONObject obj = new JSONObject(true);
//        for (int i = 0; i < keys.size(); i++) {
//            String key = keys.get(i);
//            String value = params.getString(key);
//            if(value!=null) {
//            	obj.put(key, value);
//            }
//        }
//        String result = obj.toString();
        String result = params.toString();
        result = AesUtil.encryptToBase64Third(result, appSecret);
        return result;
    }

    /**
     * 通过appSecret解密参数
     *
     * @param params
     * @param appSecret
     * @return
     */
    public  static JSONObject paramsDecrypt(String params, String appSecret) {
        params = AesUtil.decryptFromBase64Third(params, appSecret);
        JSONObject result = JSONObject.parseObject(params);
        return result;
    }
    
    /**
     * 通过appSecret解密参数
     * @param params
     * @param appSecret
     * @return
     */
    public  static JSONObject paramsDecryptWithOrder(String params, String appSecret) {
        params = AesUtil.decryptFromBase64Third(params, appSecret);
        JSONObject result = JSONObject.parseObject(params, Feature.OrderedField);
        return result;
    }

    /**
     * 生成本地签名
     *
     * @param params
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static String generateSign(JSONObject params, String appSecret) throws IllegalArgumentException {
        List<String> keys = new ArrayList<String>(params.keySet());
        keys.remove("signCode");
        Collections.sort(keys);
        StringBuffer result = null;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (i == 0) result = new StringBuffer();
            else result.append("&");
            String value = params.getString(key);
            if(value!=null) {
            	result.append(key).append("=").append(value);
            }
        }
        result.append("&appSecret=" + appSecret);
        return params == null ? null : MD5.md5(result.toString());
    }
    
   /* public static void main(String[] args) {
    	JSONObject obj = new JSONObject(false);
    	obj.put("a", "");
    	obj.put("b", 123);
    	obj.put("c", null);
    	System.out.println(obj.toString());
	}*/
}
