package com.jxtb.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jxtb.mp.dao.UserMapper;
import com.jxtb.mp.entity.User;
import com.jxtb.mp.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by jxtb on 2019/12/1.
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

}
