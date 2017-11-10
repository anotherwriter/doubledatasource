package com.example.demo.test.crawler;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CrawlerTestMain {


    public static void main(String[] args){

        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        new SimpleConnectionListener(webClient);

        try {
            final HtmlPage htmlPage = webClient.getPage("http://www.iqiyi.com");

            // TODO check anchor href. let illegal to be legal
            HtmlAnchor dianYingAnchor = htmlPage.getAnchorByText("电影");
            HtmlPage dianYingPage = webClient.getPage("http:" + dianYingAnchor.getHrefAttribute());

            HtmlAnchor freeMovieAnchor = dianYingPage.getAnchorByText("免费电影");
            HtmlPage freeMoviePage = webClient.getPage(freeMovieAnchor.getHrefAttribute());


            System.out.println("爱奇艺首页 title: " + htmlPage.getTitleText());

            System.out.println("电影 href: " + dianYingAnchor.getHrefAttribute());
            System.out.println("电影 title: " + dianYingPage.getTitleText());

            System.out.println("free电影 href: " + freeMovieAnchor.getHrefAttribute());
            System.out.println("free电影 title: " + freeMoviePage.getTitleText());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 得到一个认证https链接的HttpClient对象（因为我们将要的天涯登录是Https的）
     * @return
     * @throws Exception
     */
    private static HttpClient getSSLInsecureClient() throws Exception {
        // 建立一个认证上下文，认可所有安全链接，当然，这是因为我们仅仅是测试，实际中认可所有安全链接是危险的
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return HttpClients.custom().//
                setSSLSocketFactory(sslsf)//
                // .setProxy(new HttpHost("127.0.0.1", 8888))
                .build();
    }

}
