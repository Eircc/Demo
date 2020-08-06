package com.kapcb.ccc.service.impl;

import com.kapcb.ccc.domian.User;
import com.kapcb.ccc.service.IUserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <a>Title:IUserServiceImpl</a>
 * <a>Author：<a>
 * <a>Description：<a>
 *
 * @author ccc
 * @version 1.0.0
 * @date 2020/8/6 21:15
 */
public class IUserServiceImpl implements IUserService {
    private static final String DEMO_ONE = "1";

    public List<User> getUserList(String id) {
        if (DEMO_ONE.equals(id)) {
            User kapcb = new User("kapcb", "123456", "eircccallroot@163.com", "1111222333", new Date());
            User eircc = new User("eircc", "123456", "eircccallroot@yeah.net", "11112222333", new Date());
            return Arrays.asList(kapcb,eircc);
        } else {
            return null;
        }
    }
}
