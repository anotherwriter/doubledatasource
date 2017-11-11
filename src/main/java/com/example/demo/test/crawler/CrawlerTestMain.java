package com.example.demo.test.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class CrawlerTestMain {


    public static void main(String[] args){

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        new SimpleConnectionListener(webClient);

        try {
            final HtmlPage htmlPage = webClient.getPage("http://www.iqiyi.com");

            Thread.sleep(2 * 1000);
            // TODO check anchor href. let illegal to be legal
            HtmlAnchor dianYingAnchor = htmlPage.getAnchorByText("电影");
            HtmlPage dianYingPage = webClient.getPage("http:" + dianYingAnchor.getHrefAttribute());

            Thread.sleep(2 * 1000);
            HtmlAnchor freeMovieAnchor = dianYingPage.getAnchorByText("免费电影");
            HtmlPage freeMoviePage = webClient.getPage(freeMovieAnchor.getHrefAttribute());

            // 获取文档中所有的div
            List<HtmlDivision> htmlDivisions = (List<HtmlDivision>) freeMoviePage.getByXPath("//div");

            // 通过css选择器Selector获取元素
            // 获取当前页所有class="site-piclist_pic"的元素 返回DOM节点列表
            DomNodeList<DomNode> divDomNodes = freeMoviePage.querySelectorAll(".site-piclist_pic");
            List<Movie> movies = new ArrayList<>(divDomNodes.size());

            for (DomNode domNode : divDomNodes) {
                HtmlAnchor a = domNode.querySelector("a");
                String href = a.getAttribute("href").trim();
                String title = a.getAttribute("title").trim();

                Movie tmpMovie = new Movie();
                tmpMovie.setTitle(title);
                tmpMovie.setUrl(href);
                tmpMovie.setFrom("www.iqiyi.com");

                movies.add(tmpMovie);
            }

            System.out.println("爱奇艺首页 title: " + htmlPage.getTitleText());

            System.out.println("电影 href: " + dianYingAnchor.getHrefAttribute());
            System.out.println("电影 title: " + dianYingPage.getTitleText());

            System.out.println("free电影 href: " + freeMovieAnchor.getHrefAttribute());
            System.out.println("free电影 title: " + freeMoviePage.getTitleText());

            for (Movie movie : movies) {
                System.out.println(movie);
            }

            Thread.sleep(4 * 1000);
            webClient.close();
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class Movie {
        private String title; // 片名
        private String url; // 链接

        private String score; // 评分
        private int time; // 时长
        private String from; // 来源
        private String info; // 视频介绍
        private String tag; // 视频标签

    }

}
