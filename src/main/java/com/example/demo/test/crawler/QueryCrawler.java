package com.example.demo.test.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Slf4j
public class QueryCrawler {
    public static void main(String[] args) {
        long timeStamp = (long) Math.floor(new Date().getTime() * Math.random());
        String params = "?source=input&sr=" + timeStamp;
        String keyWord = "虫师";
        try {
            keyWord = URLEncoder.encode("虫师", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String queryUrl = "http://so.iqiyi.com/so/q_" + keyWord + params;

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false); // script脚本执行异常时 不抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false); // 获取失败的状态码时 不抛出异常
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
                DomNodeList<DomNode> domNodes = resultPage.querySelectorAll("li[class=\"list_item\"]");
                log.info("domNodes={}", domNodes);
            } else {
                log.error("resultPage is null!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        webClient.close();

    }
}
