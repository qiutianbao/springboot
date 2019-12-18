package com.jxtb.mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jxtb on 2019/11/30.
 * 用户实体类
 */
@Data
@TableName("sys_user")
public class User implements Serializable{
    /**
     * 主键
     */
    @TableId
    private Long userId;
    /**
     * 姓名
     */
    @TableField("name")
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 直属上级
     */
    private Long managerId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本
     */
    @Version
    private Integer version;

    /**
     * 逻辑删除标识(0：未删除，1：已删除)
     */
    @TableLogic
    private Integer deleted;

    /**
     * 备注
     */
    private static String remark;

    public static String getRemark() {
        return remark;
    }

    public static void setRemark(String remark) {
        User.remark = remark;
    }

}
