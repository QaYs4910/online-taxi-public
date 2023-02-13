package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.dto.ResponseResult;

public interface TokenService {
    /**
     * 刷新token
     * @param refreshTokenSrc
     * @return
     */
    ResponseResult tokenRefresh(String refreshTokenSrc);
}
