package com.mashibing.apipassenger.service.imp;

import com.mashibing.apipassenger.service.VerificationCodeService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeServiceImp implements VerificationCodeService {

    @Override
    public String generatorCode(String passengerPhone) {
        /**
         * 1.调用远程验证码服务,获取验证码
         * 2.存如redis
         */
        System.out.println("调用验证码服务,获取验证码");
        String code = "1111";
        System.out.println("存入redis");


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",1);
        jsonObject.put("message","success");
        return jsonObject.toString();
    }

}
