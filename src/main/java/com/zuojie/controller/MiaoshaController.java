package com.zuojie.controller;

import com.zuojie.domain.MiaoshaOrder;
import com.zuojie.domain.MiaoshaUser;
import com.zuojie.rabbitmq.MQSender;
import com.zuojie.rabbitmq.MiashaMessage;
import com.zuojie.redis.GoodsKey;
import com.zuojie.redis.RedisService;
import com.zuojie.result.CodeMsg;
import com.zuojie.result.Result;
import com.zuojie.service.GoodsService;
import com.zuojie.service.MiaoshaService;
import com.zuojie.service.MiaoshaUserService;
import com.zuojie.service.OrderService;
import com.zuojie.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @zuthor:zuojie
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    MQSender mqSender;

    private Map<Long,Boolean> localMap=new HashMap<Long, Boolean>();

    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, MiaoshaUser user,
                       @RequestParam("goodsId")long goodsId) {
            model.addAttribute("user", user);
        if(user == null) {
            //return "login";
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //内存标记减少redis访问

        boolean over = localMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
       long stock= redisService.decr(GoodsKey.getMiaoshaGoodsStock,""+goodsId);
        if(stock<0){
            localMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order= orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiashaMessage mm=new MiashaMessage();
        mm.setMiaoshaUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        //0表示 排队中
        return Result.success(0);

        //
        /*//判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);*/
       // return "order_detail";
    }

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList==null||goodsList.size()==0){
            return;
        }
        for (GoodsVo goods:goodsList
             ) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getStockCount());
            localMap.put(goods.getId(),false);

            
        }


    }
    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

}
