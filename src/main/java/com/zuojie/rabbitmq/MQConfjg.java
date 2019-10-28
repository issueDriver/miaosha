package com.zuojie.rabbitmq;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfjg {
    public static final String MIAOSHA_QUEUE = "m.queue";
    public static final String QUEUE = "queue";
    public static final String HEADERS_QUEUE="headers.queue";
    public static final String TOPIC_QUEUE1="topic.queue1";
    public static final String  TOPIC_QUEUE2="topic.queue2";
    public static final String  TOPIC_EXCHANGE="topicExchange";
    public static final String FANOUT_EXCAHNGE="fanoutExchange";
    public static final String HEADERS_EXCAHNGE="headersExchange";


    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue MIAOSHA_QUEUE() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

    /**
     * topic 模式交换机
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1,true);
    }
    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2,true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }

    /**
     *Fanout 模式 交换机Exchange
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCAHNGE);
    }
    @Bean
    public Binding FanoutBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());

    }
    @Bean
    public Binding FanoutBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());

    }
    /**
     * Headers模式 交换机Exchange
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCAHNGE);
    }
    @Bean
    public Queue headersQueue1(){
        return new Queue(HEADERS_QUEUE,true);
    }
    @Bean
    public Binding headersBinding(){
        Map<String,Object>map=new HashMap<String, Object>();
        map.put("header1","values1");
        map.put("header2","values2");
        return BindingBuilder.bind(headersQueue1()).to(headersExchange()).whereAll(map).match();

    }






}
