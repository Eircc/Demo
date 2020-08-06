package com.kapcb.ccc.start;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * <a>Title:MainApplication</a>
 * <a>Author：<a>
 * <a>Description：<a>
 *
 * @author ccc
 * @version 1.0.0
 * @date 2020/8/6 21:39
 */
public class ProviderMainApplication {
    private static final String CONFIG_LOCATION = "provider.xml";

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        ioc.start();
        System.in.read();
    }
}
