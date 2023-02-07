package com.mashibing.servicepassengeruser.service.imp;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicepassengeruser.dao.entity.PassengerUser;
import com.mashibing.servicepassengeruser.dao.mapper.PassengerUserMapper;
import com.mashibing.servicepassengeruser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final PassengerUserMapper passengerUserMapper;
    /**
     * 通过手机号进行注册和登录
     * @param passengerPhone 乘客手机号
     * @return
     */
    @Override
    public ResponseResult loginOrRegister(String passengerPhone) {
        System.out.println("UserService 被调用..手机号:"+passengerPhone);
        //1.根据手机号查询用户
        HashMap<String, Object> map = new HashMap<>();
        map.put("passenger_phone",passengerPhone);
        List<PassengerUser> passengerUsers = this.passengerUserMapper.selectByMap(map);
        System.out.println(passengerUsers == null?"无记录":passengerUsers.get(0).getPassengerPhone());
        //2.判断用户是否存在
        //3.如果不存在插入一条新纪录
        return ResponseResult.success();
    }
}
