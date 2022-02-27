package org.javaboyer.vhr.controller;

import org.javaboyer.vhr.config.MenuService;
import org.javaboyer.vhr.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller是接口层用来响应数据，具体业务处理应交由service层处理，而数据库交互交由mybatis处理
 *
 * @author zhangfu.huang
 * @date 2022年02月27日 8:44
 */
@RestController
@RequestMapping("/system/config")
public class SystemConfigController {

    @Autowired
    public MenuService menuService;

    @RequestMapping("/menu")
    public List<Menu> getMenusByHrId() {
        return menuService.getMenusByHrId();

    }
}
