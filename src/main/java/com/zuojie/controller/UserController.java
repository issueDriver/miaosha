package com.zuojie.controller;

import com.zuojie.domain.MiaoshaUser;
import com.zuojie.redis.RedisService;
import com.zuojie.result.Result;
import com.zuojie.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @zuthor:zuojie
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }
}
