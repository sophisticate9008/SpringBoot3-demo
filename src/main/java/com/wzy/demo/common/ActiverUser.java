package com.wzy.demo.common;

import java.lang.reflect.Method;
import java.util.List;

import com.wzy.demo.entity.User;

import lombok.Data;

@Data
public class ActiverUser {
    User user;
    List<String> permissions;
    List<String> roles;
    
    // public static <T> ResultObj isActiveUser(T entity) {
    //     try {
    //         User activUser = (User) WebUtils.getSession().getAttribute("user");
    //         if (activUser != null) {
    //             Method getAccountMethod = entity.getClass().getMethod("getAccount");
    //             String entityAccount = (String) getAccountMethod.invoke(entity);
    //             if(entityAccount != null) {
    //                 if(entityAccount.equals(activUser.getAccount())) {
    //                     return null;
    //                 }
    //             }
                
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
            
    //     }
    //     return ResultObj.Permission_Exceed;
    // }
}


