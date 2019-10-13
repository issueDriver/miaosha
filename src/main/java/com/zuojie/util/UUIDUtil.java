package com.zuojie.util;

import java.util.UUID;
/**
 * @zuthor:zuojie
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
