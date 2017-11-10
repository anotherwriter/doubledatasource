package com.example.demo.test.crawler;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.FalsifyingWebConnection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SimpleConnectionListener extends FalsifyingWebConnection {


    public SimpleConnectionListener(WebClient webClient) throws IllegalArgumentException {
        super(webClient);
    }

    @Override
    public WebResponse getResponse(WebRequest request) throws IOException {
        WebResponse response = super.getResponse(request);

        String url = response.getWebRequest().getUrl().toString();

        if (log.isDebugEnabled()) {
            log.debug("下载文件链接：" + url);
        }
        // check url

        return response;
    }
}
