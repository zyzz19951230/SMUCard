package com.jsutech.zyzz.smucard.network;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.jsutech.zyzz.smucard.db.models.UserProfile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zyzz on 3/22/18.
 *
 */

public class Helpers {

    private static final String TAG = "Helpers";
    // 枚举类型，用于定义Http请求方法，目前支持5种常见请求方法
    public enum HttpMethod {
        GET, POST, PUT, PATCH, DELETE
    }
    // for calcPWD()
    private final static String KEY_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    private static int _count = 0;
    // 正则表达式，从网页中解析（提取）数据时使用
    // 匹配字符串中的整数或小数
    private static Pattern NUMBER_IN_STRING = Pattern.compile("^\\D*([-+]?[\\d]+(\\.[\\d]+)?)\\D*");
    // 空请求体
    private final static RequestBody BLANK_REQUEST_BODY = RequestBody.create(MediaType.parse("text/plain"), "");


    // 从网站js中移植过来的算法，用以计算password的加密值
    // js源：function lyf(input) - login.js
    static String calcPWD(String input) {
        if (input == null || input.length() < 1) {
            return "";
        }
        StringBuilder output = new StringBuilder();

        boolean _chr2NaN = false;
        boolean _chr3NaN = false;

        int chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        chr1 = 0;
        chr2 = 0;
        chr3 = 0;

        enc1 = 0;
        enc2 = 0;
        enc3 = 0;
        enc4 = 0;

        int i = 0;

        do {
            chr1 = input.codePointAt(i++);
            if (i < input.length()) {
                _chr2NaN = false;
                chr2 = input.codePointAt(i++);
            } else {
                i++;
                _chr2NaN = true;
            }
            if (i < input.length()) {
                _chr3NaN = false;
                chr3 = input.codePointAt(i++);
            } else {
                i++;
                _chr3NaN = true;
            }


            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;

            if (_chr2NaN) {
                enc3 = 64;
                enc4 = 64;
            } else {
                if (_chr3NaN) {
                    enc4 = 64;
                }
            }

            output.append(KEY_STR.charAt(enc1)).append(KEY_STR.charAt(enc2)).append(KEY_STR.charAt(enc3)).append(KEY_STR.charAt(enc4));
            chr1 = 0;
            chr2 = 0;
            chr3 = 0;
            enc1 = 0;
            enc2 = 0;
            enc3 = 0;
            enc4 = 0;

        } while (i < input.length());

        if (_count < 4) {
            _count++;
            output = new StringBuilder(calcPWD(output.toString()));
        } else {
            _count = 0;
        }
        return output.toString();
    }

    // 移除字符串中的"&nbsp;"
    private static String removeNBSP(String input){
        return input.replaceAll("&nbsp;", "");
    }

    // 判断页面是否为登录后的首页
    static boolean isIndexPage(String responseBody){
        return  responseBody.contains("JavaScript:mainFrame.doForward('userInfo.action')") &&
                responseBody.contains("JavaScript:mainFrame.doForward('powerPayInit.action')") &&
                responseBody.contains("JavaScript:mainFrame.doForward('userSrunQuery.action')");

    }

    // 判断验证码是否正确
    static boolean isCheckCodeWrong(String responseBody){
        //return responseBody.contains("alert('验证码错误');");
        return responseBody.contains("'验证码错误'");
    }

    // 从网页中解析用户信息
    static UserProfile parseUserProfile(String responseBody){
        Document doc = Jsoup.parse(responseBody);
        Element table = doc.selectFirst("table#itb");
        Log.d(TAG, responseBody);
        // 无法在页面中找到包含用户基本信息的表
        if (table == null){
            return null;
        }

        // 解析网页中的table标签，并将其转换为UserProfile对象
        Elements td = table.select("tr > td");
        UserProfile userProfile = new UserProfile();
        for (Element e : td){
            Element tag = e.selectFirst("div.tb_item");
            if (tag != null){
                if (tag.html().contains("姓名")){
                    userProfile.setName(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("押金")){
                    userProfile.setDeposit( removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("性别")){
                    userProfile.setGender(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("帐户状态")){
                    userProfile.setAccountStatus(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("创建日期")){
                    userProfile.setCreatedDate(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("国家")){
                    userProfile.setNationality(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("银行卡号")){
                    userProfile.setCreditCard(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("身份：")){
                    userProfile.setRole(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("地址")){
                    userProfile.setAddress(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("类别")){
                    userProfile.setClassification(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("编号")){
                    userProfile.setSUID(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("有效日期")){
                    userProfile.setEffectiveDate(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("部门")){
                    userProfile.setDepartment(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("身份证号")){
                    userProfile.setPID(removeNBSP(e.nextElementSibling().html()));
                }
                if (tag.html().contains("有效区域")){
                    userProfile.setEffectiveArea(removeNBSP(e.nextElementSibling().html()));
                }
            }
        }
        return userProfile;
    }

    // 从网页中解析网络计费信息和网络计费充值表单
    static SrunPay parseSrunPay(String responseBody, boolean parseInfo, boolean parsePayForm){
        if (!parseInfo && !parsePayForm){
            return null;
        }
        Document document = Jsoup.parse(responseBody);
        Element infoTable = document.selectFirst("table#mytable");
        Element payForm = document.selectFirst("form#userSrunPay");

        if (infoTable == null || payForm == null){
            return null;
        }
        SrunPay srunPay = new SrunPay();

        if (parseInfo){
            // 从网页中的Table标签解析学号、姓名、余额、状态、主卡状态等信息
            Elements tds = infoTable.select("td");
            List<String> eleTextList = tds.eachText();
            if (eleTextList.size() >= 5){
                // 学号
                srunPay.setSUID(eleTextList.get(0));
                // 姓名
                srunPay.setName(eleTextList.get(1));
                // 账户余额
                srunPay.setBal(eleTextList.get(2));
                // 状态
                srunPay.setStatus(eleTextList.get(3));
                // 主卡状态
                srunPay.setCardStatus(eleTextList.get(4));
            }
            // 从网页form标签中解析电子钱包余额、套餐名称、套餐余额等信息
            Elements p = payForm.select("p");
            for(String s : p.eachText()){
                if (s.contains("电子钱包余额")){
                    // 解析电子钱包余额
                    Matcher matcher = NUMBER_IN_STRING.matcher(s);
                    if (matcher.matches()){
                        // required api 26
                        // srunPay.seteWalletBal(matcher.group("number"));
                        srunPay.seteWalletBal(matcher.group(1));
                    }
                } else if (s.contains("套餐名称")){
                    String[] bs = s.split("，");
                    if (bs.length > 1){
                        // 解析套餐名称
                        int pos = bs[0].indexOf("：");
                        if (pos > -1){
                            srunPay.setBillingPackage(bs[0].substring(pos + "：".length()).trim());
                        } else {
                            srunPay.setBillingPackage(bs[0]);
                        }
                        // 解析套餐余额
                        Matcher matcher = NUMBER_IN_STRING.matcher(bs[1]);
                        if (matcher.matches()){
                            // required api 26
                            // srunPay.setPackageBal(matcher.group("number"));
                            srunPay.setPackageBal(matcher.group(1));
                        } else {
                            srunPay.setPackageBal(bs[1]);
                        }
                    }
                }
            }
        }

        if (parsePayForm){
            // 解析网络计费充值表单
            Elements inputs = payForm.select("input");
            for (Element e : inputs){
                // 跳过submit（提交）按钮
                if (!e.attr("type").equals("submit")){
                    srunPay.addToSrunPayForm(e.attr("name"), e.attr("value"));
                }
            }
        }

        return srunPay;
    }

    // 从网页中解析账户余额信息
    static String parseAccountBal(String responseBody){
        Document doc = Jsoup.parse(responseBody);
        Element div = doc.selectFirst("div#tip");

        if (div == null)
            return null;

        String text = div.html();
        Matcher matcher = NUMBER_IN_STRING.matcher(text);
        if (matcher.matches()){
            return matcher.group(1);
        }
        return null;
    }

    // 获取绝对url
    static String getAbsUrl(String baseUrl, String action){
        if (!baseUrl.endsWith("/")){
            baseUrl += "/";
        }
        if (action.startsWith("/")){
            action = action.substring(1);
        }
        return baseUrl + action;
    }

    // 快速创建异常对象的工具方法
    static IllegalArgumentException notNullException(String filedName){
        return new IllegalArgumentException(filedName +" cannot be null.");
    }

    static IllegalArgumentException notBlankException(String filedName){
        return new IllegalArgumentException(filedName +" cannot be blank.");
    }

    // 从给定参数组建一个Request对象
    static Request prepareRequest(HttpMethod method, HttpUrl url, Headers headers, RequestBody requestBody, Object tag) throws IllegalArgumentException {
        if (url == null){
            throw Helpers.notNullException("url");
        }
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            builder.headers(headers);
        }
        switch (method) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(requestBody == null ? BLANK_REQUEST_BODY : requestBody);
                break;
            case PUT:
                builder.put(requestBody != null ? requestBody : BLANK_REQUEST_BODY);
                break;
            case PATCH:
                builder.patch(requestBody != null ? requestBody : BLANK_REQUEST_BODY);
                break;
            case DELETE:
                if (requestBody != null)
                    builder.delete(requestBody);
                else
                    builder.delete();
                break;
            default:
                throw new IllegalArgumentException("unsupported http method.");
        }
        if (null != tag) {
            builder.tag(tag);
        }
        // 生成request对象
        return builder.build();
    }


}
