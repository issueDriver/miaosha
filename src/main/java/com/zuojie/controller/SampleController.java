package com.zuojie.controller;

import com.zuojie.domain.User;
import com.zuojie.redis.RedisService;
import com.zuojie.redis.UserKey;
import com.zuojie.result.Result;
import com.zuojie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @zuthor:zuojie
 */
@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","zuojie");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
      //  User  user  = redisService.get(UserKey.getById, ""+1, User.class);
       // return Result.success(user);
        return null;

    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user  = new User();
        user.setId(1);
        user.setName("1111");
       // redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
      //  redisService.set(UserKey.getById, ""+1, user);
       // return Result.success(true);
        return null;
    }


}
