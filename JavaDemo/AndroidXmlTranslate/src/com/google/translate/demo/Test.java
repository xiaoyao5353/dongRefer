package com.google.translate.demo;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class Test {
    public static void main(String[] args) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet hg = new HttpGet("https://translate.google.cn/");
        try {
            httpClient.execute(hg);
            List<Cookie> cookies = ((AbstractHttpClient) httpClient)
                    .getCookieStore().getCookies();
            for (Cookie c : cookies) {
                System.out.println(c.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
