package org.javaboyer.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboyer.vhr.model.Hr;
import org.javaboyer.vhr.model.RespBean;
import org.javaboyer.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
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
import java.io.PrintWriter;

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
     * configure配置用户请求认证
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
        // 配置请求认证规则，所有的请求必须经过该认证规则才能访问服务
        http.authorizeRequests()
                // 所有请求被认证后才能访问
                .anyRequest().authenticated()
                .and()
                // 基于表单登录的身份验证
                .formLogin()
                // 指定验证参数：用户名参数，用户密码参数
                .usernameParameter("username")
                .passwordParameter("password")
                // 验证用户登录的URL，本例是验证用户名和密码
                .loginProcessingUrl("/doLogin")
                // 指定登录页面。当有没经过登录页面和登录验证页面的请求都会被拦截后端跳转到/login页面
                .loginPage("/login")
                // 登录成功的回调
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        // 设置响应内容类型
                        resp.setContentType("application/json; charset=UTF-8");
                        // 向客户端发送字符文本(响应)的PrintWriter对象。利用PrinterWriter构造响应body
                        PrintWriter out = resp.getWriter();
                        // 被认证的用户实例
                        Hr hr = (Hr) authentication.getPrincipal();
                        // 封装登录成功响应信息
                        RespBean ok = RespBean.ok("登录成功", hr);
                        // 转为json字符串
                        String s = new ObjectMapper().writeValueAsString(ok);
                        // 将用户实例json字符串写入响应体中
                        out.write(s);
                        // 发送响应
                        out.flush();
                        out.close();
                    }
                })
                // 登录失败回调
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                        resp.setContentType("application/json; charset=UTF-8");
                        PrintWriter out = resp.getWriter();
                        RespBean error = RespBean.error("登录失败");
                        if (e instanceof LockedException) {
                            error.setMsg("账户被锁定，请联系管理员");
                        } else if (e instanceof DisabledException) {
                            error.setMsg("账户被禁用，请联系管理员");
                        } else if (e instanceof CredentialsExpiredException) {
                            error.setMsg("账户密码过期，请联系管理员");
                        } else if (e instanceof AccountExpiredException) {
                            error.setMsg("账户过期，请联系管理员");
                        } else if (e instanceof BadCredentialsException) {
                            error.setMsg("用户名或密码错误");
                        }

                        out.write(new ObjectMapper().writeValueAsString(error));
                        out.flush();
                        out.close();
                    }
                })
                // 允许任何用户访问本接口。确保failureUrl(String)以及HttpSecurityBuilder的 url、 getLoginPage和getLoginProcessingUrl被授予对任何用户的访问权限。
                .permitAll()
                .and()
                // 注销登录，默认的url是/logout
                .logout()
                .logoutUrl("/logout")
                // 注销登录成功后的处理
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json; charset=UTF-8");
                        PrintWriter out = resp.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                // postman测试相关
                .csrf().disable();
    }
}
