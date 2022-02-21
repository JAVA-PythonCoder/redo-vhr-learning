package org.javaboyer.vhr.config;

import org.javaboyer.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SecurityConfig是自定义Spring security配置类，必须扩展WebSecurityConfigurerAdapter，重写其暴露出来的方法将自定义配置注册到SpringBoot容器中
 *
 * @author zhangfu.huang
 * @date 2022年02月21日 22:52
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    HrService hrService;

    /**
     * PasswordEncoder接口用于密码加密，BCryptPasswordEncoder是PasswordEncoder的实现
     *
     * @author zhangfu.huang
     * @date 2022/2/21 22:58
     * @return org.springframework.security.crypto.password.PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * configure用于身份验证。AuthenticationManagerBuilder的实例调用UserDetailsService的实例方法loadUserByUsername，该方法返回UserDetails实例。
     * 用户认证的核心还是UserDetails实例中的方法会被spring security调用。
     *
     * @author zhangfu.huang
     * @date 2022/2/21 23:00
     * @param auth
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    /**
     * configure配置用户认证成功后的页面跳转信息。
     *
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     *
     * <pre>
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * </pre>
     * <p>
     * Any endpoint that requires defense against common vulnerabilities can be specified here, including public ones.
     * See {@link HttpSecurity#authorizeRequests} and the `permitAll()` authorization rule
     * for more details on public endpoints.
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()  //  所有请求被认证后才能访问
                .and()
                .formLogin()  // 表单登录
                .usernameParameter("username")  // 指定：用户名参数，用户密码参数
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")  // 登录处理的Url
                .loginPage("/login")  // 登录页面
                .successHandler(new AuthenticationSuccessHandler() {
                    // 登录成功的回调
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    // 登录失败回调
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

                    }
                })
                .permitAll()  //  跟本接口相关都直接返回
                .and()
                .logout()  // 退出登录
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    //  退出登录成功后的处理
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

                    }
                })
                .permitAll()
                .and()
                .csrf().disable();  // 跟postman测试相关
    }
}
