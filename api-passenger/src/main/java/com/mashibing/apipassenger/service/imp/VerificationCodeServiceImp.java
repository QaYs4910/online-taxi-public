package com.mashibing.apipassenger.service.imp;


import com.alibaba.cloud.commons.lang.StringUtils;
import com.mashibing.apipassenger.remote.ServicePassengerClient;
import com.mashibing.apipassenger.remote.ServiceVerificationCodeClient;
import com.mashibing.apipassenger.service.VerificationCodeService;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.constant.TokenTypeConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.VerificationCodeDTO;
import com.mashibing.internalcommon.response.NumberCodeResponse;
import com.mashibing.internalcommon.response.TokenResponse;
import com.mashibing.internalcommon.util.JwtUtils;
import com.mashibing.internalcommon.util.RedisPrefixUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImp implements VerificationCodeService {


    /**
     * 验证码的生成位数
     */
    private static final int NUMBER_CODE_SIZE = 6;
    //remote client interface serviceVerificationCode
    private final ServiceVerificationCodeClient serviceVerificationCodeClient;
    //remote client interface servicePassenger
    private final ServicePassengerClient servicePassengerClient;
    //StringRedisTemplate
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 根据手机号生成验证码
     * @param passengerPhone 手机号
     * @return 验证码信息
     */
    @Override
    public ResponseResult generatorCode(String passengerPhone) {

        /**
         * 1.调用远程验证码服务,获取验证码
         * 2.存如redis
         */
        System.out.println("调用验证码服务,获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = this.serviceVerificationCodeClient.numberCode(NUMBER_CODE_SIZE);
        int numberCode = numberCodeResponse.getData().getNumberCode();

        System.out.println("remote number code:"+numberCode);
        System.out.println("存入redis");
        //key,value,过期时间
        //1.将对应手机号的作为key,生成的验证码作为value值写入到redis中
        String key = RedisPrefixUtils.generatorKey(passengerPhone);
        this.stringRedisTemplate.opsForValue().set(key,numberCode+"",2, TimeUnit.MINUTES);
        System.out.println("redisKey:"+key);
        //2.通过短信服务商将短信发送至客户端

        //返回值
        return ResponseResult.success();
    }

    /**
     * 根据手机号和验证码进行验证
     * @param passengerPhone 手机号
     * @param verificationCode 验证码
     * @return
     */
    @Override
    public ResponseResult checkCode(String passengerPhone, String verificationCode) {
        /**
         * 1.根据手机号查询redis中的手机验证码是否存在
         * 2.检验验证码是否是否正确
         * 3.判断用户是否登录 调用 passenger-user服务进行登录或注册
         * 4.颁发令牌
         */

        /**
         * 1.生成key
         * 2.获取value
         */
        String key = RedisPrefixUtils.generatorKey(passengerPhone);
        String codeRedis = this.stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value:"+codeRedis);

        System.out.println("检验验证码是否是否正确");
        if(StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        if(!codeRedis.trim().equals(verificationCode.trim())){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(), CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        System.out.println("判断用户是否登录");
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
        ResponseResult responseResult = this.servicePassengerClient.loginOrRegister(verificationCodeDTO);

        System.out.println("颁发令牌");
        /**
         * 双token的检验
         *
         * accessToken 成功的token每次都会检验该token
         * refreshToken 刷新使用的token在accessToken失效之后使用
         * refreshToken 要比accessToken时常要比accessToken稍微长一些
         *
         */
        String accessToken = JwtUtils.generatorToken(passengerPhone, IdentityConstants.PASSENGER_PHONE,TokenTypeConstants.ACCESS_TOKEN_TYPE);
        String refreshToken = JwtUtils.generatorToken(passengerPhone, IdentityConstants.PASSENGER_PHONE,TokenTypeConstants.REFRESH_TOKEN_TYPE);
        /**
         * 根据身份标识和tokenType生成tokenRedisKey,将数据保存在redis
         */
        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(passengerPhone, IdentityConstants.PASSENGER_PHONE,TokenTypeConstants.ACCESS_TOKEN_TYPE);
        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(passengerPhone, IdentityConstants.PASSENGER_PHONE,TokenTypeConstants.REFRESH_TOKEN_TYPE);

        this.stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,30,TimeUnit.DAYS);
        this.stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,31,TimeUnit.DAYS);

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponse);
    }

}
