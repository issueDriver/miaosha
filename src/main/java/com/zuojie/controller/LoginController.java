package com.zuojie.controller;

import com.zuojie.redis.RedisService;
import com.zuojie.result.Result;
import com.zuojie.service.MiaoshaUserService;
import com.zuojie.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
/**
 * @zuthor:zuojie
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

   @Autowired
   MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        userService.login(response, loginVo);
        return Result.success(true);
    }

}
