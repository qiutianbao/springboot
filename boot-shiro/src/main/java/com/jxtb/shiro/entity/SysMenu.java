package com.jxtb.shiro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jxtb on 2019/1/31.
 */
@Entity
public class SysMenu implements Serializable {
    @Id
    @GeneratedValue
    private Integer menuId;
    private String menuName;

    @ManyToMany
    @JoinTable(name="SysRoleMenu",joinColumns={@JoinColumn(name="menuId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private List<SysRole> roleList;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }
}
