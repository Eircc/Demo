package com.kapcb.ccc.service.impl;

import com.kapcb.ccc.domian.User;
import com.kapcb.ccc.service.IOrderService;
import com.kapcb.ccc.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

/**
 * <a>Title:IOrderServiceImpl</a>
 * <a>Author：<a>
 * <a>Description：<a>
 *
 * @author ccc
 * @version 1.0.0
 * @date 2020/8/6 21:51
 */
@Service
@RequiredArgsConstructor
public class IOrderServiceImpl implements IOrderService {

    private Logger logger = Logger.getLogger(String.valueOf(this.getClass()));

    private final IUserService iUserService;

    public void getInfo(String id) {
        logger.info("远程调用开始！");
        List<User> userList = this.iUserService.getUserList(id);
        for (User user : userList) {
            System.out.println(user);
        }
        logger.info("远程调用成功！");
    }
}
