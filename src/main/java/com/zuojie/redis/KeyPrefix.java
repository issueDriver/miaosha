package com.zuojie.redis;
/**
 * @zuthor:zuojie
 */
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();
}
