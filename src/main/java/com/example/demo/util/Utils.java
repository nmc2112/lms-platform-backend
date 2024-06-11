package com.example.demo.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    public static boolean isLong(String str) {
        if (str == null) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
