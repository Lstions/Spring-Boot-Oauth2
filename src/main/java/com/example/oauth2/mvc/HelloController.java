package com.example.oauth2.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private String str;

    @GetMapping("/hello")
    public String hello(){
        return str;
    }


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
