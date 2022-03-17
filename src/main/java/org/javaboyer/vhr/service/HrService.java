package org.javaboyer.vhr.service;

import org.javaboyer.vhr.mapper.HrMapper;
import org.javaboyer.vhr.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * HrService用于后台用户登录验证。验证过程借助spring security，所以需要其实现UserDetailsService接口方法返回UserDetails实例
 *
 * @author zhangfu.huang
 * @date 2022年02月21日 22:42
 */
@Service
public class HrService implements UserDetailsService {

    @Autowired
    HrMapper hrMapper;

    /**
     * 根据用户名从数据库中取出用户并封装为实例
     *
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username);
        // 查询得到的hr是否存在，如果不存在说明当前登录用户不合法，存在则对登录用户赋权限（用户拥有的角色）
        if (hr == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 用户登录成功，为用户赋权限
        System.out.println("hr.getId() = " + hr.getId());
        hr.setRoles(hrMapper.getHrRolesById(hr.getId()));
        return hr;
    }
}
