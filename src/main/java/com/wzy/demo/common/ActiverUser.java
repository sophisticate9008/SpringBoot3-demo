package com.wzy.demo.common;

import java.util.List;

import com.wzy.demo.entity.User;

import lombok.Data;

@Data
public class ActiverUser {
    User user;
    List<String> permissions;
    List<String> roles;
}
