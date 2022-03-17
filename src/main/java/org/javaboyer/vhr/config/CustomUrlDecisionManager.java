package org.javaboyer.vhr.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * AccessDecisionManager接口访问控制
 *
 * @author zhangfu.huang
 * @date 2022年03月08日 11:22
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    /**
     * 解决参数传递的访问控制决策
     * authentication：调用方法的调用者（当前登录对象）
     * object：被调用的安全对象（请求对象）
     * configAttributes：被调用的安全对象关联的配置属性（该值由FilterInvocationSecurityMetadataSource拦截url预处理后返回的请求对象的配置属性集合ConfigAttributes）
     * <p>
     * Resolves an access control decision for the passed parameters.
     *
     * @param authentication   the caller invoking the method (not null)
     * @param object           the secured object being called
     * @param configAttributes the configuration attributes associated with the secured
     *                         object being invoked
     * @throws AccessDeniedException               if access is denied as the authentication does not
     *                                             hold a required authority or ACL privilege
     * @throws InsufficientAuthenticationException if access is denied as the
     *                                             authentication does not provide a sufficient level of trust
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // 遍历请求对象(object)关联的配置属性
        for (ConfigAttribute configAttribute : configAttributes) {
            // 获取请求对象需要匹配上的角色
            String needRole = configAttribute.getAttribute();
            // 匹配角色是“ROLE_LOGIN”说明当前用户可能没有登录或请求的url不存在
            if (needRole.equals("ROLE_LOGIN")) {
                // 当前登录对象是否是匿名登录对象，是说明没有登录，抛出拒绝访问异常，不是直接返回
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登录，请登录!");
                } else {
                    // 如果url不存在直接返回
                    return;
                }
            }
            // authentication.getAuthorities()获取当前登录用户的权限集合
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // 遍历权限集合，判断当前登录对象已有的权限是否满足请求对象关联的配置属性，如果满足则返回
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        // 请求对象关联的配置属性与当前登录用户已有权限都不匹配，抛出拒绝访问异常
        throw new AccessDeniedException("权限不足，请联系管理员");


    }

    /**
     * Indicates whether this <code>AccessDecisionManager</code> is able to process
     * authorization requests presented with the passed <code>ConfigAttribute</code>.
     * <p>
     * This allows the <code>AbstractSecurityInterceptor</code> to check every
     * configuration attribute can be consumed by the configured
     * <code>AccessDecisionManager</code> and/or <code>RunAsManager</code> and/or
     * <code>AfterInvocationManager</code>.
     * </p>
     *
     * @param attribute a configuration attribute that has been configured against the
     *                  <code>AbstractSecurityInterceptor</code>
     * @return true if this <code>AccessDecisionManager</code> can support the passed
     * configuration attribute
     */
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    /**
     * 指示AccessDecisionManager的实现是否能够为指定的安全对象类型提供访问控制决策
     *
     * Indicates whether the <code>AccessDecisionManager</code> implementation is able to
     * provide access control decisions for the indicated secured object type.
     *
     * @param clazz the class that is being queried
     * @return <code>true</code> if the implementation can process the indicated class
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
