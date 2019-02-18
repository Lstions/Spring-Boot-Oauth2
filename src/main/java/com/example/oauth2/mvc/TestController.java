package com.example.oauth2.mvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/me")
    public String me(){
        return "this is me";
    }
}
