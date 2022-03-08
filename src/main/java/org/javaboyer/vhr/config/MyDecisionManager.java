package org.javaboyer.vhr.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * @author zhangfu.huang
 * @date 2022年03月08日 11:22
 */
public class MyDecisionManager implements AccessDecisionManager {
    /**
     * 解决参数传递的访问控制决策
     *
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
