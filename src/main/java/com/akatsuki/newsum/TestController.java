package com.akatsuki.newsum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "록리: 쓰레기가 노력의 힘으로 천재를 꺾는다..!!";
    }
}
