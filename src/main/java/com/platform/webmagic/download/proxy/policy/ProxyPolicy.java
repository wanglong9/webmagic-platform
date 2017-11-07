package com.platform.webmagic.download.proxy.policy;

import us.codecraft.webmagic.proxy.Proxy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.platform.util.LogUtil;

/**
 * 代理策略
 *
 * @author 刘太信
 * @date 2017年8月15日 下午5:16:58
 */
public interface ProxyPolicy {
    int retryTime = 3;

    /**
     * 获取实例化对象锁
     * @return
     */
    ReentrantLock getReentrantLock();

    /**
     * 获取实例化对象锁条件
     * @return
     */
    default Condition getCondition() {
        return getReentrantLock().newCondition();
    }

    /**
     * 创建新代理
     *
     * @return http代理会返回ip端口帐号密码(当返回null请download执行清除代理设置)，adsl代理永远返回null
     * @author 刘太信
     * @date 2017年8月15日 下午6:09:17
     */
    Proxy createProxy();

    /**
     * 获取当前代理
     * @return
     */
    Proxy getCurrentProxy();

    /**
     * 是否已切换
     *
     * @return
     * @description
     * @author 王龙
     * @date 2017年11月1日 下午1:11:11
     */
    AtomicBoolean isChanging();

    /**
     * 设置IsChanging的值
     *
     * @param flag
     */
    void setIsChanging(Boolean flag);

    /**
     * 获取新代理对象
     * @return
     */
    default Proxy getNewProxy() {
        Proxy proxy = null;
        if (isChanging().compareAndSet(false, true)) {
            LogUtil.info("开始切换代理" + this);
            proxy = createProxy();
            signalAll();
        }
        await();
        return proxy;
    }

    /**
     * 等待
     *
     * @author 刘太信
     * @date 2017年8月15日 下午6:50:16
     */
    default void await() {
        if (isChanging().get()) {
            try {
                getReentrantLock().lock();
                while (isChanging().get()) {
                    try {
                        getCondition().await();
                    } catch (InterruptedException e) {
                    }
                }
            } finally {
                getReentrantLock().unlock();
            }
        }
    }

    /**
     * 释放
     *
     * @author 刘太信
     * @date 2017年8月15日 下午6:50:25
     */
    default void signalAll() {
        try {
            getReentrantLock().lock();
            setIsChanging(false);
            getCondition().signalAll();
        } finally {
            getReentrantLock().unlock();
        }
    }
}
