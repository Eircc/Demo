package com.kapcb.ccc.test;

import com.kapcb.ccc.service.IOrderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * <a>Title:ConsumerMainApplication</a>
 * <a>Author：<a>
 * <a>Description：<a>
 *
 * @author ccc
 * @version 1.0.0
 * @date 2020/8/6 22:00
 */
public class ConsumerMainApplication {

    private static final String TEST_ID = "1";
    private static final String CONFIG_LOCATION = "consumer.xml";

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        ioc.start();
        IOrderService bean = ioc.getBean(IOrderService.class);
        bean.getInfo(TEST_ID);
        System.in.read();
    }
}
