package com.http;

import java.util.HashMap;  
import java.util.Map;  
//对接口进行测试  
public class TestMain {  
//    private String url = "https://192.168.1.101/";
    private String url = "https://translate.google.cn/#zh-CN/zh-TW/商城模式";
    private String charset = "utf-8";  
    private HttpClientUtil httpClientUtil = null;  
      
    public TestMain(){  
        httpClientUtil = new HttpClientUtil();  
    }  
      
    public void test(){  
        String httpOrgCreateTest = url/* + "httpOrg/create"*/;  
        Map<String,String> createMap = new HashMap<String,String>();  
        createMap.put("authuser","*****");  
        createMap.put("authpass","*****");  
        createMap.put("orgkey","****");  
        createMap.put("orgname","****");  
        String httpOrgCreateTestRtn = httpClientUtil.doPost(httpOrgCreateTest,createMap,charset);  
        System.out.println("result:"+httpOrgCreateTestRtn);  
    }  
      
    public static void main(String[] args){  
        TestMain main = new TestMain();  
        main.test();  
    }  
}