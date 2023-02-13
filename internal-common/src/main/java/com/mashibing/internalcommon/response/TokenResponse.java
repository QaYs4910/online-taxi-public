package com.mashibing.internalcommon.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
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
