package com.wzy.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class SystemController {
    @RequestMapping("index")
    public String index(){
        return "system/index";
    }
}
