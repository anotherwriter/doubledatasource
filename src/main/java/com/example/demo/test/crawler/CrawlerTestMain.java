package com.example.demo.test.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Slf4j
public class CrawlerTestMain {


    public static void main(String[] args){
        Logger.getLogger("com.gargoylesoftware").setLevel(org.apache.log4j.Level.WARN);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.SEVERE);
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false); // script脚本执行异常时 不抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false); // 获取失败的状态码时 不抛出异常
        webClient.getOptions().setPopupBlockerEnabled(true);
        //new SimpleConnectionListener(webClient);

        final String homePageUrl = "http://www.iqiyi.com";

        try {
//            final HtmlPage htmlPage = webClient.getPage(homePageUrl);
//            System.out.println("爱奇艺首页 title: " + htmlPage.getTitleText());
//
//            if (null == htmlPage) {
//                System.out.println("htmlPage is null");
//                return;
//            }

//            Thread.sleep(4 * 1000);
//            // TODO check anchor href. let illegal to be legal
//            HtmlAnchor dianYingAnchor = htmlPage.getAnchorByText("电影");
//            HtmlPage dianYingPage = webClient.getPage("http:" + dianYingAnchor.getHrefAttribute());
//
//            System.out.println("电影 href: " + dianYingAnchor.getHrefAttribute());
//            System.out.println("电影 title: " + dianYingPage.getTitleText());
//
//            Thread.sleep(4 * 1000);
//            HtmlAnchor freeMovieAnchor = dianYingPage.getAnchorByText("免费电影");
//            HtmlPage freeMoviePage = webClient.getPage(freeMovieAnchor.getHrefAttribute());
            HtmlPage freeMoviePage = webClient.getPage("http://list.iqiyi.com/www/1/----------0---11-1-1-iqiyi--.html");
//            System.out.println("free电影 href: " + freeMovieAnchor.getHrefAttribute());
            System.out.println("free电影 title: " + freeMoviePage.getTitleText());

            List<Movie> movies = new ArrayList<>();
            int pageNo = 1;
            for (; ;) {
                if (pageNo == 3) {
                    break;
                }
                System.out.println("当前页数: " + pageNo);
                HtmlAnchor nextPageAnchor = freeMoviePage.getAnchorByText("下一页");
                if (null == nextPageAnchor) {
                    break;
                }
                getMovieData(movies, freeMoviePage, webClient);
                freeMoviePage = nextPageAnchor.click();
                pageNo ++;
            }

            Thread.sleep(4 * 1000);
            webClient.close();

            for (Movie movie : movies) {
                System.out.println(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getMovieData(List<Movie> movies, HtmlPage freeMoviePage, final WebClient webClient) {
        // 获取文档中所有的div 不管他们在文档中的位置
//            List<HtmlDivision> htmlDivisions = (List<HtmlDivision>) freeMoviePage.getByXPath("//div[@class='site-piclist_pic']");
//            for (HtmlDivision htmlDivision : htmlDivisions) {
//                HtmlAnchor a = htmlDivision.getFirstByXPath("a");
//                String href = a.getAttribute("href").trim();
//                String title = a.getAttribute("title").trim();
//            }

        // 通过css选择器Selector获取元素
        // 获取当前页所有class="site-piclist_pic"的元素 返回DOM节点列表
        DomNodeList<DomNode> picDivDomNodes = freeMoviePage.querySelectorAll(".site-piclist_pic");
        DomNodeList<DomNode> infoDivDomNodes = freeMoviePage.querySelectorAll(".site-piclist_info");

        try {
            int times = 0;
            for (DomNode domNode : picDivDomNodes) {
                times ++;
                if (times == 3) {
                    break;
                }
                Movie tmpMovie = new Movie();

                HtmlAnchor a = domNode.querySelector("a");
                String href = a.getAttribute("href").trim();
                String title = a.getAttribute("title").trim();

                if (!StringUtils.isEmpty(href)) {
                    HtmlPage speicalMoviePage = webClient.getPage(href);
                    Thread.sleep(4 * 1000);

                    // get the movie's tag
                    DomElement tagSpan = speicalMoviePage.getElementById("datainfo-taglist");
                    if (null != tagSpan) {
                        DomNodeList<DomNode> tags = tagSpan.getChildNodes();
                        String tagStr = "";
                        for (DomNode tag : tags) {
                            tagStr += tag.getTextContent().trim() + " ";
                        }
                        tmpMovie.setTag(tagStr);
                    }

                    // get the movie's tag info
                    DomElement infoSpan = speicalMoviePage.getElementById("data-videoInfoDes");
                    if (null != infoSpan) {
                        tmpMovie.setInfo(infoSpan.getTextContent().trim());
                    }

                    speicalMoviePage.cleanUp();
                }

                HtmlImage img = a.getFirstByXPath("img");
                String thumbnail = img.getSrcAttribute();

//                HtmlElement timeSpan = a.getFirstByXPath("span[@class='icon-vInfo']");
                HtmlElement timeSpan = domNode.querySelector("span");
                if (null != timeSpan)
                    tmpMovie.setTime(timeSpan.getTextContent().trim());

                tmpMovie.setTitle(title);
                tmpMovie.setUrl(href);
                tmpMovie.setThumbnail(thumbnail);
                tmpMovie.setFrom("www.iqiyi.com");

                movies.add(tmpMovie);
            }

            times = 0;
            for (int i = 0; i < infoDivDomNodes.size(); i++) {
                times ++;
                if (times == 3) {
                    break;
                }

                Movie tmpMovie = movies.get(i);
                DomNode domNode = infoDivDomNodes.get(i);

                HtmlElement scoreSpan = domNode.querySelector("span");
//                HtmlElement scoreSpan = domNode.getFirstByXPath("span[@class='score']");
                if (null != scoreSpan)
                    tmpMovie.setScore(scoreSpan.getTextContent().trim());
            }

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
        private String thumbnail;

        private String score; // 评分
        private String time; // 时长
        private String from; // 来源
        private String info; // 视频介绍
        private String tag; // 视频标签


    }

}
