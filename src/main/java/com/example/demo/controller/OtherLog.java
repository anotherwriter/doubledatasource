package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/other")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OtherLog {

    @RequestMapping(method = RequestMethod.GET, value = "{str}")
    @ResponseStatus(HttpStatus.OK)
    public String getUser(@PathVariable("str") String s) {

        return "input: " + s;
    }

}
