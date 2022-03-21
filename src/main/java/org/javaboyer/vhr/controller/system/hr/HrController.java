package org.javaboyer.vhr.controller.system.hr;

import org.javaboyer.vhr.model.Hr;
import org.javaboyer.vhr.model.RespBean;
import org.javaboyer.vhr.model.Role;
import org.javaboyer.vhr.service.HrService;
import org.javaboyer.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangfu.huang
 * @date 2022年03月21日 8:42
 */
@RestController
@RequestMapping("/system/hr")
public class HrController {
    @Autowired
    HrService hrService;
    @Autowired
    RoleService roleService;

    @GetMapping("/")
    public List<Hr> getAllHrs() {
        return hrService.getAllHrs();
    }

    /**
     * 用户信息更新都通过该接口完成
     *
     * @param hr
     * @return org.javaboyer.vhr.model.RespBean
     * @author zhangfu.huang
     * @date 2022/3/21 13:33
     */
    @PutMapping("/")
    public RespBean updateHr(@RequestBody Hr hr) {
        if (hrService.updateHr(hr) == 1) {
            return RespBean.ok("用户信息更新成功");
        }
        return RespBean.error("用户信息更新失败");
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/role")
    public RespBean updateHrWithRoles(Integer hrId, Integer[] roleIds) {
        if (hrService.updateHrWithRoles(hrId, roleIds)) {
            return RespBean.ok("用户角色更新成功");
        }
        return RespBean.error("用户角色更新失败");
    }

    @GetMapping("/role/{hrId}")
    public List<Role> getHrRoles(@PathVariable Integer hrId) {
        return hrService.getHrRoles(hrId);
    }
}
