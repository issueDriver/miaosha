package com.zuojie.service;

import com.zuojie.domain.MiaoshaOrder;
import com.zuojie.domain.MiaoshaUser;
import com.zuojie.domain.OrderInfo;
import com.zuojie.redis.MiaoshaKey;
import com.zuojie.redis.RedisService;
import com.zuojie.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * @zuthor:zuojie
 */
@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional()
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
       boolean success= goodsService.reduceStock(goods);
       if(success){
           //order_info maiosha_order
           return orderService.createOrder(user, goods);
       }else {
           setGoodsOver(goods.getId());
           return null;
       }


    }



    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        System.out.println("++++"+order);

        if(order!=null){
            //秒杀成功
            return  order.getOrderId();
        }else {
         boolean isOver= getGoodsOver(goodsId);
         if(isOver){
             return -1;

         }else {
             return 0;

         }

        }


    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver,""+goodsId);


    }
    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver,""+goodsId,true);

    }
}
