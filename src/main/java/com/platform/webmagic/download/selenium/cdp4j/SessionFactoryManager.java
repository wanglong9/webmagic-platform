package com.platform.webmagic.download.selenium.cdp4j;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.session.SessionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class SessionFactoryManager {

    private static Logger logger = LoggerFactory.getLogger(SessionFactory.class);
    /**
     * 获取浏览器session工厂
     *
     * @description
     * @author 王龙
     * @date 2017年10月10日 上午10:50:35
     * @return
     */
    public static SessionFactory getFactory() {
        return getFactory(false);
    }

    /**
     * 获取浏览器session工厂
     *
     * @description
     * @author 王龙
     * @date 2017年10月10日 上午10:50:35
     * @return
     */
    public static SessionFactory getFactory(boolean isHeadless) {
        Launcher launcher = new Launcher();
        SessionFactory factory = null;
        if (isHeadless) {
            // 代理且无窗口
            factory = launcher.launch(Arrays.asList("--headless", "--disable-gpu"));
            logger.info("创建sessionFactory：无代理IP，无视图窗口");
        } else {
            // 代理测试，但有窗口
            factory = launcher.launch();
            logger.info("创建sessionFactory：无代理IP，有视图窗口");
        }
        return factory;
    }

    /**
     * 获取浏览器session工厂
     *
     * @description
     * @author 王龙
     * @date 2017年10月10日 上午10:52:07
     * @param proxy
     * @param isHeadless
     * @return
     */
    public static SessionFactory getFactory(String proxy) {
        return getFactory(proxy, false);
    }

    /**
     * 获取浏览器session工厂
     *
     * @description
     * @author 王龙
     * @date 2017年10月10日 上午10:52:07
     * @param proxy
     * @param isHeadless
     * @return
     */
    public static SessionFactory getFactory(String proxy, boolean isHeadless) {
        Launcher launcher = new Launcher();
        SessionFactory factory = null;
        if (StringUtils.isNotEmpty(proxy)) {
            if (isHeadless) {
                // 代理且无窗口
                factory = launcher
                        .launch(Arrays.asList("--headless", "--disable-gpu", "--proxy-server=\"http=" + proxy + "\""));
                logger.info("创建sessionFactory：有代理IP【{}】，无视图窗口", proxy);
            } else {
                // 代理测试，但有窗口
                factory = launcher.launch(Arrays.asList("--proxy-server=\"http=" + proxy + "\""));
                logger.info("创建sessionFactory：有代理IP【{}】，有视图窗口", proxy);
            }
        } else {
            factory = getFactory(isHeadless);
        }
        return factory;
    }

    public static void close(SessionFactory factory) {
        if (null != factory) {
            factory.close();
        }
    }
}
