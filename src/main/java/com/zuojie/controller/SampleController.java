package com.zuojie.controller;

import com.zuojie.domain.User;
import com.zuojie.rabbitmq.MQSender;
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

    @Autowired
    MQSender mqSender;
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","zuojie");
        return "hello";
    }
   /* @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello,xiaopang");
        return Result.success("hello ,Word");

    }*/
    /*@RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout(){
        mqSender.sendFanout("hello,xiaogao");
        return Result.success("hello ,Word");

    }
    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic(){
        mqSender.sendTopic("hello,xiaofan");
        return Result.success("hello ,Word");

    }
    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> headers(){
        mqSender.sendHeaders("hello,xiaowang");
        return Result.success("hello ,Word");

    }*/

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
       User  user  = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);


    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user  = new User();
        user.setId(1);
        user.setName("1111");
        //UserKey:id1
        redisService.set(UserKey.getById, ""+1, user);
        redisService.set(UserKey.getById, ""+1, user);
        return Result.success(true);

    }


}
