package org.javaboyer.vhr.service;

import org.javaboyer.vhr.mapper.MenuRoleMapper;
import org.javaboyer.vhr.mapper.RoleMapper;
import org.javaboyer.vhr.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangfu.huang
 * @date 2022年03月17日 15:56
 */
@Service
public class RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    MenuRoleMapper menuRoleMapper;

    public List<Role> getAllRoles() {
        return roleMapper.getAllRoles();
    }

    public Integer addRole(Role role) {
        String name = role.getName();
        if (!name.startsWith("ROLE_")) {
            name = "ROLE_" + name;
            role.setName(name);
        }
        return roleMapper.insertSelective(role);

    }

    @Transactional
    public Integer deleteRole(Integer id) {
        if (roleMapper.deleteByPrimaryKey(id) == 1) {
            menuRoleMapper.deleteByRid(id);
            return 1;
        }
        return 0;
    }
}
