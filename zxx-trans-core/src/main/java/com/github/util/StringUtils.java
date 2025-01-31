package com.github.util;
/**
 * StringUtils: 字符串工具类
 *
 * @author zhangxiaoxiang
 * @since 2025/1/31
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}
