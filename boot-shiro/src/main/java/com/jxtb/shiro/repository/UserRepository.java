package com.jxtb.shiro.repository;

import com.jxtb.shiro.entity.SysUser;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jxtb on 2019/1/31.
 */
public interface UserRepository  extends CrudRepository<SysUser,Long> {

    SysUser findByUserName(String username);

}
