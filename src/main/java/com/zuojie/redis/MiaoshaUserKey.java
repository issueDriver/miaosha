package com.zuojie.redis;

/**
 * @author:zuojie
 */
public class MiaoshaUserKey extends BasePrefix {
    //过期时间设置为两天
    public static final int TOKEN_EXPIRE = 3600*24 * 2;
    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
    public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");
}
