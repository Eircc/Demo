package com.kapcb.ccc.service;

import com.kapcb.ccc.domian.User;

import java.util.List;

/**
 * <a>Title:IUserService</a>
 * <a>Author：<a>
 * <a>Description：<a>
 *
 * @author ccc
 * @version 1.0.0
 * @date 2020/8/6 21:10
 */
public interface IUserService {
    /**
     * 根据id获取用户信息
     *
     * @param id String
     * @return User
     */
    List<User> getUserList(String id);
}
