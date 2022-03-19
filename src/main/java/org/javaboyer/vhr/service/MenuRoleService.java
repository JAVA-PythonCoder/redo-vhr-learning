package org.javaboyer.vhr.service;

import org.javaboyer.vhr.mapper.MenuRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangfu.huang
 * @date 2022年03月18日 10:40
 */
@Service
public class MenuRoleService {

    @Autowired
    MenuRoleMapper menuRoleMapper;

    public List<Integer> selectMidsByRid(Integer rid) {
        return menuRoleMapper.selectMidsByRid(rid);

    }

    /**
     * -@Transactional事务管理注解
     *
     * @author zhangfu.huang
     * @date 2022/3/18 12:48
     * @param rid
     * @param mids
     * @return boolean
     */
    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        // 更新角色对应的菜单权限，先将角色对应的所有菜单权限删除后在向其中添加角色权限。
        menuRoleMapper.deleteByRid(rid);
        Integer result = menuRoleMapper.insertRecords(rid, mids);
        return result == mids.length;
    }
}
