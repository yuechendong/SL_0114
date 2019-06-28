package com.sy.interceptor;

import com.sy.model.common.User;
import com.sy.tools.Constants;
import com.sy.tools.RedisAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    RedisAPI redisAPI;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //想访问的链接
        String forwardUrl = request.getRequestURI();
        System.out.println("===>"+forwardUrl);

        //允许访问的链接
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        String key = "FUN"+sessionUser.getRoleId();
        boolean flag = redisAPI.exist(key);
        if(flag){
            String allowedUrl = redisAPI.get(key);
            System.out.println("====>"+allowedUrl);
            if(allowedUrl.contains(forwardUrl)){
                //允许访问
                return true;
            }
        }

        //无访问权限
        request.getRequestDispatcher("/WEB-INF/pages/401.jsp").forward(request, response);
        return false;
    }
}
