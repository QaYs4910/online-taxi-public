package com.mashibing.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mashibing.internalcommon.dto.TokenResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    //盐
    private static final String SIGN = "#^&*$^(#&";
    private static final String JWT_KEY = "phone";
    //身份表示区分 乘客1 ,司机2
    private static final String JWT_KEY_IDENTITY = "identity";

    private static final String JWT_TOKEN_TYPE = "tokenType";
    //根据手机号的和身份标识来区分是乘客端还是司机端
    public static String  generatorToken(String passengerPhone,String identity,String tokenType){

        Map<String,String> map = new HashMap<>();
        map.put(JWT_KEY,passengerPhone);
        map.put(JWT_KEY_IDENTITY,identity);
        map.put(JWT_TOKEN_TYPE,tokenType);
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

        //4.整合过期时间(token使用redis的过期时间,取消JWT的有效时间)
//        builder.withExpiresAt(date);
        //5.生成token(使用HMAC256)
        String sign = builder.sign(Algorithm.HMAC256(SIGN));
        return sign;
    }
    //解析token
    public static TokenResult DeCodeJWTparseToken(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();
        return new TokenResult().setPhone(phone).setIdentity(identity);
    }

    public static void main(String[] args) {
        System.out.println("生成token");
        String s = JwtUtils.generatorToken("17521209671","1","");
        System.out.println(s);
        TokenResult tokenResult = JwtUtils.DeCodeJWTparseToken(s);
        System.out.println(tokenResult.getIdentity());
        System.out.println(tokenResult.getPhone());
    }
}
