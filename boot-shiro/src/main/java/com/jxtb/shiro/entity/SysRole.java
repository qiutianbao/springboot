package com.jxtb.shiro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jxtb on 2019/1/31.
 */
@Entity
public class SysRole implements Serializable {
    @Id
    @GeneratedValue
    private Integer roleId;
    private String roleName;

    //多对多关系
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="SysRoleMenu",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="menuId")})
    private List<SysMenu> menuList;

    //多对多关系
    @ManyToMany
    @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="userId")})
    private List<SysUser> userList;// 一个角色对应多个用户

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<SysMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenu> menuList) {
        this.menuList = menuList;
    }

    public List<SysUser> getUserList() {
        return userList;
    }

    public void setUserList(List<SysUser> userList) {
        this.userList = userList;
    }
}
