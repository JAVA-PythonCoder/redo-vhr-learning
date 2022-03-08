package org.javaboyer.vhr.config;

import org.javaboyer.vhr.model.Menu;
import org.javaboyer.vhr.model.Role;
import org.javaboyer.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 用户登录成功后，访问资源会被AbstractSecurityInterceptor拦截，调用FilterInvocationSecurityMetadataSource中的方法
 * 拦截url并进行预处理返回该url对应的ConfigAttribute集合对象，再调用授权管理器AccessDecisionManager的方法根据ConfigAttribute和Spring全局缓存SecurityContextHolder获取用户权限，
 * 或根据所配策略验证信息。
 *
 *
 * @author zhangfu.huang
 * @date 2022年03月08日 9:34
 */
public class MyFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    MenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 访问安全对象的ConfigAttribute
     * <p>
     * Accesses the {@code ConfigAttribute}s that apply to a given secure object.
     *
     * @param object the object being secured
     * @return the attributes that apply to the passed in secured object. Should return an
     * empty collection if there are no applicable attributes.
     * @throws IllegalArgumentException if the passed object is not of a type supported by
     *                                  the <code>SecurityMetadataSource</code> implementation
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 因资源访问会被AbstractSecurityInterceptor拦截，调用FilterInvocationSecurityMetadataSource获取用户访问url的权限
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        // 获取所有的菜单url对应的访问角色
        List<Menu> menuWithRoles = menuService.getAllMenuWithRole();
        // 遍历所有的菜单项
        for (Menu menu : menuWithRoles) {
            // 请求的url与菜单项中的url匹配，成功则将该url对应的所有角色封装成ConfigAttribute集合返回
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                List<Role> roles = menu.getRoles();
                String[] roleStr = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    roleStr[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(roleStr);
            }
        }
        // 对于没有匹配的url，也返回ConfigAttribute集合，但标记了“ROLE_LOGIN”自定义统一表示用户没登录
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    /**
     * If available, returns all of the {@code ConfigAttribute}s defined by the
     * implementing class.
     * <p>
     * This is used by the {@link AbstractSecurityInterceptor} to perform startup time
     * validation of each {@code ConfigAttribute} configured against it.
     *
     * @return the {@code ConfigAttribute}s or {@code null} if unsupported
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 指示SecurityMetadataSource的实现类是否为指定的安全对象类型添加ConfigAttribute
     * <p>
     * Indicates whether the {@code SecurityMetadataSource} implementation is able to
     * provide {@code ConfigAttribute}s for the indicated secure object type.
     *
     * @param clazz the class that is being queried
     * @return true if the implementation can process the indicated class
     */
    @Override
    public boolean supports(Class<?> clazz) {
        // ture表示SecurityMetadataSource的实现类为指定的安全对象类型添加ConfigAttribute
        return true;
    }
}
