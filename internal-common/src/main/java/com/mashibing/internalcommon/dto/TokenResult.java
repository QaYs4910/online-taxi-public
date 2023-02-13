package com.mashibing.internalcommon.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenResult {

    /**
     *  手机号
     */
    private String phone;
    /**
     *  身份标识
     */
    private String identity;

}
