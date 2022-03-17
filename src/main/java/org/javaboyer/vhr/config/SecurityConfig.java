package org.javaboyer.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboyer.vhr.model.Hr;
import org.javaboyer.vhr.model.RespBean;
import org.javaboyer.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * SecurityConfig继承WebSecurityConfigurerAdapter用来个性化SpringSecurity的默认配置，
 * 主要是用户验证登录、资源请求管理和响应配置
 *
 *
 * SecurityConfig是自定义Spring security配置类，必须扩展WebSecurityConfigurerAdapter，重写其暴露出来的方法将自定义配置注册到SpringBoot容器中
 * SpringSecurity的简单原理：使用众多的拦截器对url拦截，以此来管理权限。其中有两个核心流程。
 * 权限管理：登录验证（用户名和密码验证）+访问资源管理（访问Url时的用户权限验证）
 * 登录验证拦截器：AuthenticationProcessingFilter，资源管理拦截器：AbstractSecurityInterceptor
 * 拦截器里的实现需要一些组件实现，有AuthenticationManager、AccessDecisionManager
 * 大体流程：用户登录请求会被AuthenticationProcessingFilter拦截，调用AuthenticationManager的实现，而且
 * AuthenticationManager会调用ProviderManager来获取用户验证信息（不同的Provider调用服务不同，因为这些信息
 * 可以在数据库上，可以是在LDAP服务器上，可以是xml配置文件上等），如果验证通过后会将用户的权限信息封装成一个
 * User放在Spring的全局缓存SecurityContextHolder中，以备后面访问资源时使用。访问资源（即授权管理），访问url时，
 * 会通过AbstractSecurityInterceptor拦截器拦截，其中会调用FilterInvocationSecurityMetadataSource的方法
 * 来获取拦截url所需的全部权限，再调用授权管理器AccessDecisionManager，这个授权管理器会通过spring的全局
 * 缓存SecurityContextHolder获取用户的权限信息，还会获取被拦截的url和被拦截url所需的全部权限，然后根据所配
 * 策略（有：一票决定，一票否决， 少数服从多数等），如果权限足够，则返回，权限不够则报错并调用权限不足页面。
 *
 *
 * @author zhangfu.huang
 * @date 2022年02月21日 22:52
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    HrService hrService;
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;

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
     * configure(WebSecurity)定义哪些URL不受SpringSecurity保护
     * 忽略某些请求url不受spring security保护
     * <p>
     * Override this method to configure {@link WebSecurity}. For example, if you wish to
     * ignore certain requests.
     * <p>
     * Endpoints specified in this method will be ignored by Spring Security, meaning it
     * will not protect them from CSRF, XSS, Clickjacking, and so on.
     * <p>
     * Instead, if you want to protect endpoints against common vulnerabilities, then see
     * {@link #configure(HttpSecurity)} and the {@link HttpSecurity#authorizeRequests}
     * configuration method.
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // SpringSecurity忽略URL匹配/login的请求
        web.ignoring().antMatchers("/login");
    }

    /**
     * configure用于登录时的用户身份验证。AuthenticationManagerBuilder的实例调用UserDetailsService的实例方法loadUserByUsername，该方法返回UserDetails实例。
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
     * configure(HttpSecurity http)对http请求进行配置管理
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
        // 配置请求认证规则，所有的请求登录后经过该认证规则才能访问服务
        http.authorizeRequests()
                // 所有请求登录后才能访问
                //.anyRequest().authenticated()
                //withObjectPostProcessor()添加请求对象处理器
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    /**
                     * Initialize the object possibly returning a modified instance that should be used
                     * instead.
                     *
                     * @param object the object to initialize
                     * @return the initialized version of the object
                     */
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        // 每个请求对象配置请求后置处理，为每个请求对象添加资源管理相关的授权管理和安全介质资源
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
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
                    /**
                     *
                     * @author zhangfu.huang
                     * @date 2022/2/26 18:28
                     * @param req spring mvc容器的请求对象，保存前端服务请求信息
                     * @param resp spring mvc容器的响应对象，保存后端服务响应信息
                     * @param authentication spring security容器的认证对象，保存被认证对象信息等。Authentication身份验证对象保存在SecurityContextHolder.getContext().getAuthentication()。
                     */
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
                .csrf().disable().exceptionHandling()
                // 请求或登录异常处理，当未登录访问接口不再重定向到/login
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException) throws IOException, ServletException {
                        resp.setContentType("application/json; charset=UTF-8");
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = RespBean.error("访问失败");
                        // 用户权限不够异常
                        if (authException instanceof InsufficientAuthenticationException) {
                            respBean.setMsg("请求失败，请联系管理员");
                        }
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();
                    }
                });
    }
}
