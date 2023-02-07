package com.mashibing.servicepassengeruser.service;

import com.mashibing.internalcommon.dto.ResponseResult;

public interface UserService {

    /**
     * 根据手机号进行登录或注册
     * 如果第一次就是注册和登录,否则就是登录
     * @param passengerPhone 乘客手机号
     * @return
     */
    ResponseResult loginOrRegister(String passengerPhone);
}
