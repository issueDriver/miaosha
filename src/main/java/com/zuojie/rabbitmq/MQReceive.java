package com.zuojie.rabbitmq;


import com.zuojie.domain.MiaoshaOrder;
import com.zuojie.domain.MiaoshaUser;
import com.zuojie.redis.RedisService;
import com.zuojie.service.GoodsService;
import com.zuojie.service.MiaoshaService;
import com.zuojie.service.OrderService;
import com.zuojie.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceive {
    private static Logger log= LoggerFactory.getLogger(MQReceive.class);
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    MiaoshaService miaoshaService;
    @RabbitListener(queues = MQConfjg.MIAOSHA_QUEUE)
    public void receive(String message){

        log.info("receive message :"+message);
        MiashaMessage miashaMessage = RedisService.stringToBean(message, MiashaMessage.class);
        MiaoshaUser user = miashaMessage.getMiaoshaUser();
        long goodsId = miashaMessage.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stockCount = goods.getStockCount();
        if(stockCount<=0){
            return ;
        }
        //判断是否已经秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order==null){
            return ;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);


    }

    /**
     * Direct模式
     * @param message
     */
    /*@RabbitListener(queues = MQConfjg.QUEUE)
    public void receive(String message){

        log.info("receive message :"+message);


    }
    @RabbitListener(queues = MQConfjg.TOPIC_QUEUE1)
    public void receiveTopic1(String message){

        log.info("receive Queue1 message :"+message);


    }

    @RabbitListener(queues = MQConfjg.TOPIC_QUEUE2)
    public void receiveTopic2(String message){

        log.info("receive Queue2 message :"+message);

    }

    @RabbitListener(queues = MQConfjg.HEADERS_QUEUE)
    public void receiveTopic3(byte[] message){

        log.info("receive Queue3 message :"+new String (message));

    }*/


}
