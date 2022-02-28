package org.javaboyer.vhr.service;

import org.javaboyer.vhr.mapper.MenuMapper;
import org.javaboyer.vhr.model.Hr;
import org.javaboyer.vhr.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
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
     *
     * @author zhangfu.huang
     * @date 2022/2/27 9:26
     * @return java.util.List<org.javaboyer.vhr.model.Menu>
     */
    public List<Menu> getMenusByHrId() {
        return menuMapper.getMenusByHrId(((Hr)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }
}
