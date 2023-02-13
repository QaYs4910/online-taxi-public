package com.mashibing.internalcommon.request;


import lombok.Data;

@Data
public class VerificationCodeDTO {
    /**
     * 乘客手机号
     */
    private String passengerPhone;
    /**
     * 验证码
     */
    private String verificationCode;
}
