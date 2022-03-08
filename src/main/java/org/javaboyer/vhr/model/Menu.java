package org.javaboyer.vhr.model;

import java.util.List;

/**
 * Menu类封装前端菜单。前端根据请求接口，后端从数据库中选出前端用户该显示的菜单。
 * url前端Url、path路由地址、component路由组件、meta路由组件是否保活和需要权限认证、children表示路由的子路由，parentId表示父菜单的id
 *
 * @author zhangfu.huang
 * @date 2022/2/26 18:14
 */
public class Menu {
    private Integer id;

    private String url;

    private String path;

    private String component;

    private String name;

    private String iconCls;

    private Meta meta;

    private List<Menu> children;

    private List<Role> roles;

    private Integer parentId;

    private Boolean enabled;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}