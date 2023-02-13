package com.mashibing.internalcommon.util;

public class RedisPrefixUtils {

    /**
     * 乘客验证码前缀
     */
    public static String verificationCodePrefix = "passenger-verification-code";
    /**
     * tokenKey的前缀信息
     */
    public static String tokenPrefix = "token-";

    /**
     * 根据手机号和身份标识生成tokenKey
     * @param passengerPhone 乘客手机号
     * @return redisKey前缀信息
     */
    public static String generatorKey(String passengerPhone){
        return verificationCodePrefix + passengerPhone;
    }

    /**
     * 生成tokenKey存入redis中区分乘客端和司机端
     * @param phone 手机号
     * @param identity 身份标识
     * @return tokenRedisKey
     */
    public static String generatorTokenKey(String phone, String identity){
        return tokenPrefix+phone+"-"+identity;
    }
}
