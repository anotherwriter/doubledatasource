package com.example.demo.test.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Slf4j
public class QueryCrawler {
    public static void main(String[] args) {
        long timeStamp = (long) Math.floor(new Date().getTime() * Math.random());
        String params = "?source=input&sr=" + timeStamp;
        String keyWord = "夏洛特烦恼";
        try {
            keyWord = URLEncoder.encode("夏洛特烦恼", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String queryUrl = "http://so.iqiyi.com/so/q_" + keyWord + params;

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false); // script脚本执行异常时 不抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false); // 获取失败的状态码时 不抛出异常
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setGeolocationEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setDoNotTrackEnabled(true);
        webClient.getOptions().setTimeout(120000);
        webClient.getOptions().setPopupBlockerEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);


        try {
            final HtmlPage searchHomePage = webClient.getPage(queryUrl);
            Thread.sleep(5 * 1000);

//            HtmlInput searchInput = searchHomePage.querySelector("input[data-suggest-elem=\"input\"]");
//            searchInput.setValueAttribute("虫师");
//            System.out.println("searchInput: " + searchInput.getValueAttribute());
//
//
//            HtmlForm htmlForm = searchHomePage.querySelector("form[data-suggest-elem=\"form\"]");
//            htmlForm.getActionAttribute();
//            System.out.println("htmlForm: " + htmlForm.getActionAttribute() + " htmlForm " + htmlForm.asXml());
//            List<HtmlForm> htmlForms = searchHomePage.getForms();
//            System.out.println("htmlForm: " + htmlForms.get(0).getActionAttribute());
//            HtmlInput searchButton = searchHomePage.querySelector("input[data-suggest-elem=\"btn\"]");
//            HtmlPage resultPage = searchButton.click();
            HtmlPage resultPage = searchHomePage;
            if (null != resultPage) {
                DomNodeList<DomNode> lis = resultPage.querySelectorAll("li.list_item");
                if (null == lis) {
                    log.error("domNodes is null");
                } else {

                    int i = 0;
                    for (DomNode li : lis) {
                        i++;
                        if (4 == i) {
                            break;
                        }
                        NamedNodeMap namedNodeMap = li.getAttributes();
                        Node movieNameNode = namedNodeMap.getNamedItem("data-widget-searchlist-tvname");
                        String movieName = movieNameNode.getNodeValue();

                        HtmlAnchor a = li.querySelector("a.figure"); // return the a with class="figure" element
                        String href = a.getHrefAttribute();

                        HtmlImage img = li.querySelector("img");
                        String imgUrl = img.getSrcAttribute();

                        log.info("movieName: {}, href: {} img: {}", movieName, href, imgUrl);
                    }
                }

            } else {
                log.error("resultPage is null!");
            }

            Thread.sleep(2 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webClient.close();

    }
}
