package com.sy.service.common;


import com.alibaba.fastjson.JSON;
import com.sy.model.common.Authority;
import com.sy.model.common.Function;
import com.sy.model.common.Menu;
import com.sy.tools.RedisAPI;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.hibernate.validator.internal.constraintvalidators.bv.time.future.FutureValidatorForYear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private RedisAPI redisAPI;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private UserService userService;


    /**
     * 根据登录用户的roleId返回可以访问菜单(主菜单和子菜单)
     *
     * 把List<Menu>放入redis中
     * @param roleId
     * @return
     */
    public String makeMenus(int roleId) throws Exception{

        String key = "MENU"+roleId;
        //1.从redis查询该key
        boolean flag = redisAPI.exist(key);
        String json = null;
        if(flag){
            json = redisAPI.get(key);
        }else{
            List<Menu> list = new ArrayList<>();
            Authority authority = new Authority();
            authority.setRoleId(roleId);
            //查询主菜单
            List<Function> mains = functionService.getMainFunctionList(authority);
            //查询子菜单
            for(Function mainFun:mains){
                mainFun.setRoleId(roleId);
                List<Function> subs = functionService.getSubFunctionList(mainFun);
                Menu menu = new Menu();
                //menu封装主菜单
                menu.setMainFunction(mainFun);
                //menu封装子菜单
                menu.setSubsFunction(subs);
                list.add(menu);
            }
            //把左边栏放入redis中
            json = JSON.toJSONString(list);
            redisAPI.set(key, json);
        }
        return json;
    }

    /**
     * 根据登录用户的roleId返回可以访问其他功能Function
     * 放入redis中
     * 把List<Functions>放入redis中
     * @param roleId
     * @return
     */
   public void makeFunctions(int roleId) throws Exception{

       String key = "FUN"+roleId;

       //1.从redis查询该key
       boolean flag = redisAPI.exist(key);
       String val = null;
       if(flag){
           val = redisAPI.get(key);
       }else{
           Authority authority = new Authority();
           authority.setRoleId(roleId);
           List<Function> allFuns = functionService.getFunctionListByRoId(authority);
           //存入redis中
           StringBuffer stringBuffer = new StringBuffer();
           for(Function function:allFuns){
               String url = function.getFuncUrl();
               stringBuffer.append(url);
           }
           val = stringBuffer.toString();
           redisAPI.set(key, val);
       }





   }

}
