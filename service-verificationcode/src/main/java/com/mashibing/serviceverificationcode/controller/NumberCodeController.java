package com.mashibing.serviceverificationcode.controller;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberCodeController {

    @GetMapping("/numberCode/{size}")
    public String numberCode(@PathVariable("size") int size){

        System.out.println("得到的参数是:"+size);
        //生成验证码
        int result = (int)((Math.random()*9+ 1) * (Math.pow(10,size - 1)));
        JSONObject object = new JSONObject();
        object.put("code",1);
        object.put("message","success");
        JSONObject object1 = new JSONObject();
        object1.put("numberCode",result);
        object.put("data",object1);
        return object.toString();
    }


    public static void main(String[] args) {
        int b = (int)((Math.random()*9+ 1) * (Math.pow(10,5)));
        System.out.println(b);
    }
}
