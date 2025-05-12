package com.mohit.urlshortener.util;

public class Base62 {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String encode(int num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62.charAt(num % 62));
            num /= 62;
        }
        return sb.reverse().toString();
    }

}
