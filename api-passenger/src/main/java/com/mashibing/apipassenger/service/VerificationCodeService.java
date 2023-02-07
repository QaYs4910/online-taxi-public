package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.dto.ResponseResult;

public interface VerificationCodeService {
    /**
     * 根据手机号申请验证码
     * @param passengerPhone 手机号
     * @return 验证码信息
     */
    ResponseResult generatorCode(String passengerPhone);

    /**
     * 根据手机号和验证码进行验证
     * @param passengerPhone 手机号
     * @param verificationCode 验证码
     * @return 返回验证信息
     */
    ResponseResult checkCode(String passengerPhone,String verificationCode);
}
