package org.javaboyer.vhr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Hr类的实例用于登录服务，所以要实现该类的登录接口，需要其实现UserDetails接口里的方法实现登录控制，即Hr类的实例在登录时spring security会调用该实例方法检查登录输入信息。
 * 因为Hr类实现了UserDetails所以在登录验证时被UserDetailsService相关方法处理返回给前端的是一个代理对象，多了一些UserDetails中提供的属性。
 *
 * @author zhangfu.huang
 * @date 2022/2/21 22:25
 */
public class Hr implements UserDetails {
    private Integer id;

    private String name;

    private String phone;

    private String telephone;

    private String address;

    private Boolean enabled;

    private String username;

    private String password;

    private String userface;

    private String remark;

    // 存储用户拥有的角色
    private List<Role> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    //    public Boolean getEnabled() {
//        return enabled;
//    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * isAccountNonExpired用于判断账户是否没有过期。
     * 在后端登录验证时会被调用，同时返回去的用户增加了accountNonExpired属性，说明返回的实例是代理的。
     *
     * @author zhangfu.huang
     * @date 2022/2/21 22:30
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * isAccountNonLocked用于判断账户是否没有被锁定。
     * 在后端登录验证时会被调用，同时返回去的用户增加了accountNonLocked属性，说明返回的实例是代理的。
     *
     * @author zhangfu.huang
     * @date 2022/2/21 22:30
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * isCredentialsNonExpired判断密码是否没有过期。
     * 在后端登录验证时会被调用，同时返回去的用户增加了accountNonExpired属性，说明返回的实例是代理的。
     *
     * @author zhangfu.huang
     * @date 2022/2/21 22:31
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * isEnabled判断用户是否启用还是禁用。
     * 在后端登录验证时会被调用，同时返回去的用户增加了enabled属性，说明返回的实例是代理的。
     *
     * @author zhangfu.huang
     * @date 2022/2/21 22:33
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        // Hr类中已定义enabled字段，直接返回
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 方法返回授予登录用户的权限。
     * 因为所有的接口通过登录用户的角色进行控制，所以登录用户的权限就是该用户拥有的角色集合。
     * 在后端登录验证时会被调用，同时返回去的用户增加了authorities属性，说明返回的实例是代理的。
     * authorities属性只在后端验证权限时有用，前端不用，所以不需要对其JSON序列化、反序列化
     *
     * @author zhangfu.huang
     * @date 2022/3/8 21:12
     * @return java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserface() {
        return userface;
    }

    public void setUserface(String userface) {
        this.userface = userface == null ? null : userface.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}