package com.mashibing.apipassenger.service.imp;

import com.mashibing.apipassenger.remote.ServiceVerificationCodeClient;
import com.mashibing.apipassenger.service.VerificationCodeService;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.response.NumberCodeResponse;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImp implements VerificationCodeService {
    //验证码的生成位数
    private static final int NUMBER_CODE_SIZE = 6;
    //remote client interface
    private final ServiceVerificationCodeClient serviceVerificationCodeClient;

    @Override
    public String generatorCode(String passengerPhone) {

        /**
         * 1.调用远程验证码服务,获取验证码
         * 2.存如redis
         */
        System.out.println("调用验证码服务,获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = this.serviceVerificationCodeClient.numberCode(NUMBER_CODE_SIZE);
        int numberCode = numberCodeResponse.getData().getNumberCode();

        System.out.println("remote number code:"+numberCode);
        System.out.println("存入redis");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",1);
        jsonObject.put("message","success");
        return jsonObject.toString();
    }

}
