package com.stumdet.config;

import com.stumdet.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class LoginHandlerIntercepter implements HandlerInterceptor {

    @Autowired
    private JWTService jwtService;

    /**
     * 在每一次请求中都应该包含第一次登陆发送的jwt
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = request.getHeader("jwt");
        Enumeration<String> headers =  request.getHeaderNames();
        String name = "";
        while((name = headers.nextElement()) != null){
            System.out.println(name);
        }
        System.out.println(jwt);
        try{
            String status = jwtService.getPayload(jwt, "status");
            if(Boolean.parseBoolean(status)){
                System.out.println("jwt验证通过");
                return true;
            }
            else{
                return false;
            }
        }
        catch(Exception e){
            System.out.println("jwt 验证不通过");
        }

        return false;
    }
}
