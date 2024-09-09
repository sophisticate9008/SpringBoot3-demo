package com.wzy.demo.common;

public class MyException {
    
    public static boolean throwRuntimeException(boolean condition, String msg) {
        if(!condition) {
            throw new RuntimeException(msg);
        }
        return condition;
    }
}
