package com.mashibing.internalcommon.dto;

import com.mashibing.internalcommon.constant.CommonStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) //链式设置
public class ResponseResult<T> {

    private int code;
    private String message;
    private T data;

    /**
     * 默认返回正确值方法
     * @return
     */
    public static ResponseResult success(){
        return new ResponseResult().setCode(CommonStatusEnum.SUCCESS.getCode()).setMessage(CommonStatusEnum.SUCCESS.getValue());
    }
    /**
     * 成功的响应
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseResult success( T data){
        return new ResponseResult().setCode(CommonStatusEnum.SUCCESS.getCode()).setMessage(CommonStatusEnum.SUCCESS.getValue()).setData(data);
    }

    /**
     * 自定义失败方法 错误码和提示
     * @param code
     * @param message
     * @return
     */
    public static ResponseResult fail(int code,String message){
        return new ResponseResult().setCode(code).setMessage(message);
    }

    /**
     * 供内部服务使用接收到数据已String的方式进行传输所以data数据类型为String
     * 自定义 失败,错误码,提示信息,具体的错误
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static ResponseResult fail(int code,String message,String data){
        return new ResponseResult().setCode(code).setMessage(message).setData(data);
    }

    /**
     * 统一失败处理
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseResult fail(T data){
        return new ResponseResult().setData(data);
    }
}
