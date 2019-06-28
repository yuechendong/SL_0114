package com.sy.controller.common;

import com.sy.model.common.User;
import com.sy.service.common.MenuService;
import com.sy.service.common.UserService;
import com.sy.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    //private static Logger logger = Logger.getLogger("LoginController");

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;

    //公告service和资讯的service,查询出相关信息,渲染到main.jsp

    @RequestMapping("/")
    public String toIndex() {

        return "index";
    }

    @RequestMapping("/main.html")
    public String toMain(){

        return "main";
    }

    @RequestMapping("/login.html")
    @ResponseBody
    public String login(User user, HttpSession session) {
        try {
            User findUser = userService.getLoginUser(user);
            if (findUser != null) {
                String json = menuService.makeMenus(findUser.getRoleId());
                menuService.makeFunctions(findUser.getRoleId());
                session.setAttribute(Constants.SESSION_USER, findUser);
                session.setAttribute("menus", json);
                return Constants.LOGIN_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //写入日志文件
            //logger.log(Level.INFO,"LoginController抛出异常" ,e);
            return Constants.LOGIN_FAILED;
        }
        return Constants.LOGIN_FAILED;

    }


}
