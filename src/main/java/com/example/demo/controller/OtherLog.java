package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/other")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OtherLog {

    @RequestMapping(method = RequestMethod.POST, value = "/test1")
    @ResponseStatus(HttpStatus.OK)
    public String testReqParam(@RequestParam(name = "a") String a) {
        log.info("testPost: a={}", a);
        return "a: " + a;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test2")
    @ResponseStatus(HttpStatus.OK)
    public String testReqBody(@RequestBody @Valid TestReq testReq) {
        log.info("testPost: testReq={}", testReq);
        return "testReq: " + testReq;
    }


    @RequestMapping(method = RequestMethod.GET, value = "{str}")
    @ResponseStatus(HttpStatus.OK)
    public String getUser(@PathVariable("str") String s) {

        return "input: " + s;
    }

}
