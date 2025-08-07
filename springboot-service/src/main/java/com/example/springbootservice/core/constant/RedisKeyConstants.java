package com.example.springbootservice.core.constant;

public class RedisKeyConstants {
    public static final String PREFIX = AppConstant.APP_NAME + ":";
    public static final String PREFIX_BLACKLIST = PREFIX + "BLACKLIST:";

    public static String blacklistKey(String tokenId) {
        return PREFIX_BLACKLIST + tokenId;
    }
}
