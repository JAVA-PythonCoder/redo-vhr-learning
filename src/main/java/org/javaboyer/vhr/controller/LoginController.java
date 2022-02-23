package org.javaboyer.vhr.controller;

import org.javaboyer.vhr.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangfu.huang
 * @date 2022年02月23日 20:25
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    public RespBean login() {
        return RespBean.error("未登录，请登录");
    }
}
