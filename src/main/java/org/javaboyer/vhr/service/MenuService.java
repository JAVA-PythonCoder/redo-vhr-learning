package org.javaboyer.vhr.service;

import org.javaboyer.vhr.mapper.MenuMapper;
import org.javaboyer.vhr.model.Hr;
import org.javaboyer.vhr.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * service层主要是业务逻辑处理相关的代码，controller层即接口层主要用来响应不做处理。
 *
 * @author zhangfu.huang
 * @date 2022年02月27日 8:49
 */
@Service
public class MenuService {

    @Autowired
    MenuMapper menuMapper;

    /**
     * 用户登录以后其相关信息保存在Authentication实例中，且对于从前端发送过来的数据默认是不可信的（即可能从其他终端发过来的）都要校验，保证数据的合规性。
     * SecurityContextHolder是Spring全局缓存
     *
     * @author zhangfu.huang
     * @date 2022/2/27 9:26
     * @return java.util.List<org.javaboyer.vhr.model.Menu>
     */
    public List<Menu> getMenusByHrId() {
        return menuMapper.getMenusByHrId(((Hr)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    /**
     * 获取菜单所有url对应的角色，即这些角色可访问这些url
     * SpringCache中@Cacheable表示方法结果会被缓存起来，缓存需要配合redis一起使用。
     * 使用换粗是为了，对于固定的查询类结果，不需要每次都去数据库查询
     *
     * @author zhangfu.huang
     * @date 2022/3/8 10:26
     * @return java.util.List<org.javaboyer.vhr.model.Menu>
     */
    //@Cacheable
    public List<Menu> getAllMenuWithRole() {
        return menuMapper.getAllMenuWithRole();
    }
}
