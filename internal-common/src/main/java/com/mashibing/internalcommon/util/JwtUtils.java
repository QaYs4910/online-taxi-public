package com.mashibing.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    //盐
    private static final String SIGN = "#^&*$^(#&";

    //生成token
    public static String  generatorToken(Map<String,String> map){
        //1.生成token的过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        Date date = calendar.getTime();
        //2.JWT的builder对象进行构建JWT信息
        JWTCreator.Builder builder = JWT.create();
        //3.整合map
        map.forEach((k,v) ->{
            builder.withClaim(k,v);
        });
        //4.整合过期时间
        builder.withExpiresAt(date);
        //5.生成token(使用HMAC256)
        String sign = builder.sign(Algorithm.HMAC256(SIGN));
        return sign;
    }
    //解析token


    public static void main(String[] args) {

        HashMap<String, String> map = new HashMap<>();
        map.put("username","zhangsan");
        map.put("age","18");
        String s = JwtUtils.generatorToken(map);
        System.out.println("生成的token:"+s);
    }
}
