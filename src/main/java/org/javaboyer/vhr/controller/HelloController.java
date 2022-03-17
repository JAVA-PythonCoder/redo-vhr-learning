package org.javaboyer.vhr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangfu.huang
 * @date 2022年02月21日 23:02
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/employee/basic/hello2")
    public String hello2() {
        return "/employee/basic/hello2";
    }

    @GetMapping("/employee/advanced/hello3")
    public String hello3() {
        return "/employee/advanced/hello3";
    }
}
