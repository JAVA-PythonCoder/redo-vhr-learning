package org.javaboyer.vhr.controller.system.basic;

import org.javaboyer.vhr.model.Menu;
import org.javaboyer.vhr.model.RespBean;
import org.javaboyer.vhr.model.Role;
import org.javaboyer.vhr.service.HrRoleService;
import org.javaboyer.vhr.service.MenuRoleService;
import org.javaboyer.vhr.service.MenuService;
import org.javaboyer.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangfu.huang
 * @date 2022年03月17日 15:55
 */
@RestController
@RequestMapping("/system/basic/permission")
public class PermissionController {

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuRoleService menuRoleService;

    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @GetMapping("/mids/{rid}")
    public List<Integer> selectMidsByRid(@PathVariable Integer rid) {
        return menuRoleService.selectMidsByRid(rid);
    }

    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        if (menuRoleService.updateMenuRole(rid, mids)) {
            return RespBean.ok("权限更新成功");
        }
        return RespBean.error("权限更新失败");
    }

    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role) {
        if (roleService.addRole(role) == 1) {
            return RespBean.ok("角色更新成功");
        }
        return RespBean.error("角色更新失败");
    }

    @DeleteMapping("/role/{id}")
    public RespBean deleteRole(@PathVariable Integer id) {
        if (roleService.deleteRole(id) == 1) {
            return RespBean.ok("角色删除成功");
        }
        return RespBean.error("角色删除失败");
    }
}
