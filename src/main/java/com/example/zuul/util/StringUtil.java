package com.example.zuul.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil extends StringUtils {

	private static final String COMMA = ",";

	/**
	 * 通过StringBuffer来组装字符串
	 * 
	 * @param strings
	 * @return
	 */
	public static String appendStrs(Object... strings) {
		StringBuffer sb = new StringBuffer();
		for (Object str : strings) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 判断所有传入参数是否非空，当传入参数长度为0，或者任意有一个为空，则返回false，所有都非空，则返回true
	 * 
	 * @param strArr
	 * @return
	 */
	public static boolean isAllNotEmpty(String... strArr) {
		boolean isAllNotEmpty = true;
		if (strArr == null || strArr.length < 1) {
			isAllNotEmpty = false;
			return isAllNotEmpty;
		}

		for (String str : strArr) {
			if (str == null || str.length() == 0) {
				isAllNotEmpty = false;
				break;
			}
		}
		return isAllNotEmpty;
	}

	/**
	 * 把list按分隔符转换成字符串
	 * 
	 * @param strList
	 *            list数据
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String turnListToStr(Collection<String> strList, String separator) {
		String result = "";
		if (strList == null || strList.size() < 1) {
			return result;
		}
		if (separator == null) {
			separator = ",";
		}

		for (String item : strList) {
			result = result + separator + item;
		}
		return result.substring(separator.length());
	}

	/**
	 * 把字符串数组按分隔符转化成字符串
	 * 
	 * @param strArr
	 *            字符串数组
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String turnArrayToStr(String separator, String... strArr) {
		String result = "";
		if (strArr == null || strArr.length < 1) {
			return result;
		}
		if (separator == null) {
			separator = ",";
		}

		for (String item : strArr) {
			result = result + separator + item;
		}
		return result.substring(separator.length());
	}

	public static String strToSecret(String str, int left, int right) {
		StringBuffer sb = new StringBuffer();
		int len = str.length() - left - right;
		if (len > 0) {
			sb.append(str.substring(0, left));
			for (int i = 0; i < len; i++) {
				sb.append("*");
			}
			sb.append(str.substring(str.length() - right));
		} else {
			return str;
		}
		return sb.toString();
	}

	public static String getNotEmptyString(String str) {
		return StringUtils.isNotEmpty(str) ? str : StringUtils.EMPTY;
	}

	public static String getLastString(String str, int num) {
		int len = str.length();
		if (len <= num) {
			return str;
		} else {
			return str.substring(len - num);
		}
	}

	public static List<String> splitToList(String source, String sep) {
		List<String> result = new ArrayList<String>();
		if (isBlank(source)) {
			return result;
		}
		String[] tempResult = source.split(sep);
		for (String item : tempResult) {
			result.add(item);
		}
		return result;
	}

	/**
	 * @方法描述：将字符串中的emoji符号转换为*
	 * 
	 * @author huyang 2017年4月6日下午12:38:04
	 * @param source
	 *            待处理字符串
	 * @return
	 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
	 */
	public static String filterEmoji(String source) {
		if (source != null) {
			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
					Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				source = emojiMatcher.replaceAll("*");
				return source;
			}
			return source;
		}
		return source;
	}

	public static String null2Str(Object str) {
		return (str != null) ? str.toString() : "";
	}

	public static String logisticsInfoDeal(String str) {
		if (str == null || "暂无".equals(str.trim())) {
			return "";
		}
		return str.trim();
	}

	/**
	 * @Title: UrlEncoder
	 * @Description: 字符串编码
	 * @param sStr
	 * @return
	 */
	public final static String UrlEncoder(String sStr) {
		String sReturnCode = "";
		try {
			sReturnCode = URLEncoder.encode(null2Str(sStr), "utf-8");
		} catch (Exception ex) {
		}
		return sReturnCode;
	}

	/**
	 * @Title: UrlDecoder
	 * @Description: 字符串解码
	 * @param sStr
	 * @return
	 */
	public static String UrlDecoder(String sStr) {
		if (isEmpty(sStr)) {
			return "";
		} else {
			String sReturnCode = sStr;
			try {
				sReturnCode = URLDecoder.decode(sStr, "utf-8");
			} catch (Exception e) {
			}
			return sReturnCode;
		}
	}


	/**
	 * 判断客户端类型
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String judgeClientDeviceFlag(String sourceStr) {
		String flagStr = "";
		if (isEmpty(sourceStr)) {
			return flagStr;
		}
		try {
			String[] sourceArray = sourceStr.split("_");
			if (sourceArray != null && sourceArray.length > 2 && isNotBlank(sourceArray[1])) {
				flagStr = sourceArray[1];
			}
			return flagStr;
		} catch (Exception e) {
			return flagStr;
		}
	}

	/**
	 * 获取指定字符串最后指定长度信息
	 * 
	 * @param cardNumber
	 * @param lastLength
	 * @return
	 */
	public static String getLastAppointLengthChar(String cardNumber, int lastLength) {
		if (cardNumber == null || cardNumber.length() <= lastLength) {
			return null2Str(cardNumber);
		}
		return cardNumber.substring(cardNumber.length() - lastLength);
	}

	public static String joinListToString(List<String> sourceList, String sep) {
	    	StringBuffer sb = new StringBuffer();
	    	if(sourceList==null || sourceList.size()==0){
	    		return "";
	    	}
	    	for (int i =0;i<sourceList.size();i++) {
	    		if(i==sourceList.size()-1){
	    			sb.append(sourceList.get(i));
	    		}else{
	    			sb.append(sourceList.get(i)).append(sep);
	    		}
			}
	    	return sb.toString();
	}
	 
	public static String getDeviceTailNum(String deviceId) {
		if (StringUtils.isEmpty(deviceId)) {
			throw new IllegalArgumentException("deviceId can't be empty.");
		}
		String deviceIdTail = deviceId.substring(deviceId.length() - 1, deviceId.length());
		char tail = deviceIdTail.charAt(0);
		int tmp = (int) tail;
		int tailNum  = tmp % 10;
		return tailNum + "";
	}

	/**
     * 根据map中数据，替换短信内容
     * 实例，假设原始短信模版中有特殊符&errorMsg,map中有对应key为errorMsg，则替换对应内容到&errorMsg
     * @param extraChar 替换内容中的标识符号，如&
     * @param originMessage 短信模版本
     * @param replaceMapData 数据封装map
     * @return
     */
    public static String convertMessageByMapInfo(String extraChar,String originMessage,Map<String,String> replaceMapData){
    	Set<String> keySet = replaceMapData.keySet();
    	for (String tempKey : keySet) {
    		originMessage = originMessage.replaceAll(extraChar+tempKey, replaceMapData.get(tempKey));
		}
    	return originMessage;
    }
    
    /**
     * 判断originStr中是否包含desStr，originStr中以splitChar间隔
     * originStr、splitChar、desStr任一个为空时，返回false
     * @param originStr
     * @param splitChar
     * @param desStr
     * @return
     */
    public static boolean isContainsSpecStr(String originStr,String splitChar,String desStr) {
    	if(StringUtil.isEmpty(originStr) || StringUtil.isEmpty(splitChar) ||StringUtil.isEmpty(desStr)) {
    		return false;
    	}
    	
    	Set<String> strArrays = new HashSet<>(Arrays.asList(originStr.split(splitChar)));
        if(strArrays.contains(desStr)){
            return true;
        }
        return false;
    }
    
    /**
     * 用户人脸图片信息处理，如果为数组json串，则直接返回
     * 如果为简单单个url，则转换为数组json串
     * 大概处理以下4种存储类型
     * 1、http://f.51fanbei.com/online/64296913bd219c56,http://f.51fanbei.com/online/8c1b1af5d9c4c1e6
     * 2、http://f.51fanbei.com/online/85c621db51ac422d.pnghttp://f.51fanbei.com/online/4585760a8160917e.png
     * 3、http://f.51fanbei.com/online/a711c5d28c9c30a2.png
     * 4、["http://f.51fanbei.com/online/8dc5eac0736225c2","http://f.51fanbei.com/online/0748aa1479b34dde"]
     * @param faceUrls
     * @return
     */
	public static List<String> processFaceUrlJson(String faceUrls) {
    	List<String> faceList = new ArrayList<String>();
    	if(faceUrls==null || faceUrls.length()==0) {
			return faceList;
    	}
    	
		if(faceUrls.contains("[") && faceUrls.contains("]")) {
			return JSON.parseArray(faceUrls, String.class);
		}
		
		if(faceUrls.contains(",")) {
			faceList = Arrays.asList(faceUrls.split(","));
			return faceList;
		}
		
		String[] arrayFaces = faceUrls.contains(".jpg")?faceUrls.split(".jpg"):null;
		String[] arrayPngFaces = faceUrls.contains(".png")?faceUrls.split(".png"):null;
		if((arrayFaces!=null && arrayFaces.length==1) || (arrayPngFaces!=null && arrayPngFaces.length==1)) {
			faceList.add(faceUrls);
			return faceList;
		}
		
		if(arrayFaces!=null && arrayFaces.length>1) {
			for (String tempFaceUrl : arrayFaces) {
				faceList.add(tempFaceUrl+".jpg");
			}
			return faceList;
		}
		
		if(arrayPngFaces!=null && arrayPngFaces.length>1) {
			for (String tempFaceUrl : arrayPngFaces) {
				faceList.add(tempFaceUrl+".png");
			}
			return faceList;
		}
		return faceList; 
    }
	
	/**
     * String字符串转成List<Long>数据格式
     * String str = "1,2,3,4,5,6" -> List<Long> listLong [1,2,3,4,5,6];
     *
     * @param strArr
     * @return
     */
    public static List<Long> stringToLongList(String strArr,String splitChar) {
        return Arrays.stream(strArr.split(splitChar))
                        .map(s -> Long.parseLong(s.trim()))
                        .collect(Collectors.toList());
    }

    /**
     * String字符串转成List<Integer>数据格式
     * String str = "1,2,3,4,5,6" -> List<Integer> listInteger [1,2,3,4,5,6];
     *
     * @param strArr
     * @return
     */
    public static List<Integer> stringToIntegerList(String strArr,String splitChar) {
        return Arrays.stream(strArr.split(splitChar))
                        .map(s -> Integer.parseInt(s.trim()))
                        .collect(Collectors.toList());
    }
    
    
	public static void main(String[] args) {
		List<String> strList = processFaceUrlJson("http://f.51fanbei.com/online/64296913bd219c56,http://f.51fanbei.com/online/8c1b1af5d9c4c1e6");
		for (String string : strList) {
			System.out.println(string);
		}
		System.out.println(isContainsSpecStr("123,34,12",",","2")); 
		System.out.println(stringToIntegerList("123,34,12",",")); 
	}
}
