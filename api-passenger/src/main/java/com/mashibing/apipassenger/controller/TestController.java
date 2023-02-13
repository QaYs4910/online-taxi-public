package com.mashibing.apipassenger.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){

        return "test";
    }

    @GetMapping("/authTest")
    public ResponseResult test1(){

        return ResponseResult.success("have token result");
    }

    @GetMapping("/noAuthTest")
    public ResponseResult test2(){

        return ResponseResult.success("do not  token result");
    }
}
