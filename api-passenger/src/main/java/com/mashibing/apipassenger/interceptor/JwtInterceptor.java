package com.mashibing.apipassenger.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.util.JwtUtils;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {

    /**
     * Jwt验证是否通过
     * @param request 请求
     * @param response 相应
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 结果返回判断
         */
        boolean result  = true;
        /**
         * 返回异常信息
         */
        String resultString = "";
        /**
         * 前端将token存入 Authorization 中 后段通过获取参数值进行解析判断
         */
        String token = request.getHeader("Authorization");
        System.out.println(token);
        //mac option command t (try catch 快捷键)
        try {
            JwtUtils.DeCodeJWTparseToken(token);
        } catch (SignatureVerificationException e) {
            resultString = "token sign error";
            result = false;
        } catch (TokenExpiredException e){
            resultString = "token time out";
            result = false;
        } catch (AlgorithmMismatchException e){
            resultString = "token AlgorithmMismatchException";
            result = false;
        } catch (Exception e){
            resultString = "token invalid";
            result = false;
        }

        //如果出现异常将异常返回前端
        if(!result){
            PrintWriter writer = response.getWriter();
            writer.print(JSONObject.fromObject(ResponseResult.fail(resultString)));
        }
        return result;
    }
}
