package com.mashibing.apipassenger.service.imp;

import com.mashibing.apipassenger.service.TokenService;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.TokenTypeConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.TokenResult;
import com.mashibing.internalcommon.response.TokenResponse;
import com.mashibing.internalcommon.util.JwtUtils;
import com.mashibing.internalcommon.util.RedisPrefixUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenServiceImp implements TokenService {

    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public ResponseResult tokenRefresh(String refreshTokenSrc) {

        //解析refreshToken
        TokenResult tokenResult = JwtUtils.checkToken(refreshTokenSrc);

        if(tokenResult == null){
            //返回错误信息
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }

        /**
         * 解析token的信息并将信息生成redisKey将value值取出
         * TokenType : accessToken,refreshToken
         */
        String phone = tokenResult.getPhone();
        String identity = tokenResult.getIdentity();

        //生成redisTokenKey
        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(phone,identity, TokenTypeConstants.REFRESH_TOKEN_TYPE);

        //读取Redis中的token
        String redisRefreshTokenKey = this.stringRedisTemplate.opsForValue().get(refreshTokenKey);

        //检验refreshToken(redis取出的key和refreshSrc不一致)
        if(StringUtils.isBlank(redisRefreshTokenKey) || !refreshTokenSrc.trim().equals(redisRefreshTokenKey.trim())){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }

        //生成双token
        String refreshToken = JwtUtils.generatorToken(phone, identity, TokenTypeConstants.REFRESH_TOKEN_TYPE);
        String accessToken = JwtUtils.generatorToken(phone, identity, TokenTypeConstants.ACCESS_TOKEN_TYPE);

        //存入redis
        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(phone,identity, TokenTypeConstants.ACCESS_TOKEN_TYPE);

//        this.stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,10,TimeUnit.SECONDS);
//        this.stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,30,TimeUnit.SECONDS);

        this.stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,30, TimeUnit.DAYS);
        this.stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,31, TimeUnit.DAYS);

        return ResponseResult.success(new TokenResponse().setRefreshToken(refreshToken).setAccessToken(accessToken));
    }

}
