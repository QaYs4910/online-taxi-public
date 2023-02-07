package com.mashibing.apipassenger.service.imp;

import com.mashibing.apipassenger.remote.ServiceVerificationCodeClient;
import com.mashibing.apipassenger.service.VerificationCodeService;
import com.mashibing.internalcommon.constant.CommonStatusConstant;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.response.NumberCodeResponse;
import com.mashibing.internalcommon.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImp implements VerificationCodeService {

    //乘客验证码前缀
    private String verificationCodePrefix = "passenger-verification-code";
    //验证码的生成位数
    private static final int NUMBER_CODE_SIZE = 6;
    //remote client interface
    private final ServiceVerificationCodeClient serviceVerificationCodeClient;
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
        String key = generatorKey(passengerPhone);
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
         * 3.判断用户是否登录
         * 4.颁发令牌
         */

        /**
         * 1.生成key
         * 2.获取value
         */
        String key = generatorKey(passengerPhone);
        String codeRedis = this.stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value:"+codeRedis);

        System.out.println("检验验证码是否是否正确");
        if(StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusConstant.VERIFICATION_CODE_ERROR.getCode(),CommonStatusConstant.VERIFICATION_CODE_ERROR.getValue());
        }
        if(!codeRedis.trim().equals(verificationCode.trim())){
            return ResponseResult.fail(CommonStatusConstant.VERIFICATION_CODE_ERROR.getCode(),CommonStatusConstant.VERIFICATION_CODE_ERROR.getValue());
        }
        System.out.println("判断用户是否登录");
        System.out.println("颁发令牌");
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken("token value");
        return ResponseResult.success(tokenResponse);
    }

    /**
     * 根据手机号生成redis的key
     * @param passengerPhone 乘客手机号
     * @return redisKey前缀信息
     */
    private String generatorKey(String passengerPhone){
        return verificationCodePrefix + passengerPhone;
    }
}
