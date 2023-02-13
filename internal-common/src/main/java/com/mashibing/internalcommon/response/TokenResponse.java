package com.mashibing.internalcommon.response;

import lombok.Data;

@Data
public class TokenResponse {
    /**
     * accessToken
     */
    private String accessToken;
    /**
     * refreshToken
     */
    private String refreshToken;
}
