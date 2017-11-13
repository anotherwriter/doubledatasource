package com.example.demo.test.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class QueryCrawler {
    public static void main(String[] args) {
        String queryUrl = "http://so.iqiyi.com";

        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false); // script脚本执行异常时 不抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false); // 获取失败的状态码时 不抛出异常
        webClient.getOptions().setPopupBlockerEnabled(true);

        try {
            final HtmlPage searchHomePage = webClient.getPage(queryUrl);
            HtmlInput searchInput = searchHomePage.querySelector("input[data-suggest-elem=\"input\"]");
            searchInput.setTextContent("虫师");
            System.out.println("searchInput: " + searchInput.getTextContent());


            List<HtmlForm> htmlForms = searchHomePage.getForms();
            System.out.println("htmlForm: " + htmlForms.get(0).getActionAttribute());
//            HtmlInput searchButton = searchHomePage.querySelector("input[data-suggest-elem=\"btn\"]");
//            HtmlPage resultPage = searchButton.click();
//            if (null != resultPage) {
//                DomNodeList<DomNode> domNodes = resultPage.querySelectorAll("li[class=\"list_item\"]");
//                log.info("domNodes={}", domNodes);
//            } else {
//                log.error("resultPage is null!");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        webClient.close();

    }
}
