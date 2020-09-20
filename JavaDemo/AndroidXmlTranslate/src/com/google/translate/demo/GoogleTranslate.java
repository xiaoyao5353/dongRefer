package com.google.translate.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.sf.json.JSONArray;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.translate.TranslaterInterface;

import com.test.OnlineXmlToXml;

public class GoogleTranslate implements TranslaterInterface {

    DefaultHttpClient httpclient;
    private static ScriptEngine engine = new ScriptEngineManager()
            .getEngineByName("JavaScript");

    public GoogleTranslate() {
        httpclient = new DefaultHttpClient();
        enableSSL(httpclient);
    }

    public static void main(String[] args) {
        GoogleTranslate googleTranslate = new GoogleTranslate();
        String str = googleTranslate.translate("stop", "en", "fa");
        System.out.println("52----" + str);
    }

    public void enableSSL(HttpClient httpclient) {
        try {
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { tm }, null);
            @SuppressWarnings("deprecation")
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme https = new Scheme("https", sf, 443);
            httpclient.getConnectionManager().getSchemeRegistry()
                    .register(https);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String tk(String val) throws Exception {
        String script = "function tk(a) {"
                + "var TKK = ((function() {var a = 561666268;var b = 1526272306;return 406398 + '.' + (a + b); })());\n"
                + "function b(a, b) { for (var d = 0; d < b.length - 2; d += 3) { var c = b.charAt(d + 2), c = 'a' <= c ? c.charCodeAt(0) - 87 : Number(c), c = '+' == b.charAt(d + 1) ? a >>> c : a << c; a = '+' == b.charAt(d) ? a + c & 4294967295 : a ^ c } return a }\n"
                + "for (var e = TKK.split('.'), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {"
                + "var c = a.charCodeAt(f);"
                + "128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)"
                + "}"
                + "a = h;"
                + "for (d = 0; d < g.length; d++) a += g[d], a = b(a, '+-a^+6');"
                + "a = b(a, '+-3^+b+-f');" + "a ^= Number(e[1]) || 0;"
                + "0 > a && (a = (a & 2147483647) + 2147483648);" + "a %= 1E6;"
                + "return a.toString() + '.' + (a ^ h)\n" + "}";

        engine.eval(script);
        Invocable inv = (Invocable) engine;
        return (String) inv.invokeFunction("tk", val);
    }

    private final static String ENCODE = "UTF-8";

    /**
     * URL 解码
     * 
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 转码
     * 
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getStringByJson(String json) {
        System.out.println("149----" + json);
        JSONArray jsonArray = JSONArray.fromObject(json);
        JSONArray segments = jsonArray.getJSONArray(0);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < segments.size(); i++) {
            String str = segments.getJSONArray(i).getString(0);
            if (!str.equals("null")) {
                result.append(str);
            }
        }
        return new String(result);
    }

    public String translate(String content, String from, String to) {
        if (from == null) {
            if(!"".equals(OnlineXmlToXml.oriValueDirSuffix)) {
                from = OnlineXmlToXml.oriValueDirSuffix;
            } else {
                from = "en";
            }
        }
        System.out
                .println("content:" + content + ";from:" + from + ";to:" + to);
        try {
            String randownCodeImageUrl = null;
            String tkStr = null;
            try {
                tkStr = tk(content);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            content = getURLEncoderString(content);
            randownCodeImageUrl = "http://translate.google.cn/translate_a/single?client=t&sl="
                    + from
                    + "&tl="
                    + to
                    + "&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&source=btn&ssel=0&tsel=0&kc=0&tk="
                    + tkStr + "&q=" + content;
            HttpGet httpget = new HttpGet(randownCodeImageUrl);
            setGetHeadPass(httpget);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            Header header = entity.getContentEncoding();
            InputStream in = entity.getContent();
            if (header != null) {
                HeaderElement[] codecs = header.getElements();
                for (int i = 0; i < codecs.length; i++) {
                    if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                        in = new GZIPInputStream(in);
                        break;
                    }
                }
            }
            httpget.releaseConnection();
            return getStringByJson(getContent(in));
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getContent(InputStream in) {
        StringBuffer sb = new StringBuffer();
        try {
            int len = -1;
            byte[] tmp = new byte[2048];
            while ((len = in.read(tmp)) != -1) {
                sb.append(new String(tmp, 0, len));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void saveContent(String destfilename, InputStream in) {
        File file = new File(destfilename);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            int len = -1;
            byte[] tmp = new byte[2048];
            while ((len = in.read(tmp)) != -1) {
                fout.write(tmp, 0, len);
            }
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (fout != null)
                    fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGetHeadPass(HttpGet post) {
        post.addHeader("(Request-Line)",
                "GET /otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand HTTP/1.1");
        post.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
        post.addHeader("Host", "translate.google.cn");
        post.addHeader("Referer", "http://translate.google.cn/");
        post.addHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
    }

    @Override
    public String translat(String content, String from, String to) {
        return translate(content, from, to);
    }
}
