package com.zuojie.redis;
/**
 * @zuthor:zuojie
 */
public class OrderKey extends BasePrefix{
    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
