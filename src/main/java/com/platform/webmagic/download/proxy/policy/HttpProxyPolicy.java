package com.platform.webmagic.download.proxy.policy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import com.platform.util.LogUtil;
import com.platform.webmagic.download.proxy.source.HttpProxySource;

import us.codecraft.webmagic.proxy.Proxy;

/**
 * http代理策略
 * 
 * @author 刘太信
 * @date 2017年8月15日 下午6:49:29
 */
public class HttpProxyPolicy implements ProxyPolicy {
    protected ReentrantLock reentrantLock = new ReentrantLock();
    protected AtomicBoolean isChanging = new AtomicBoolean(false);
    protected final HttpProxySource proxySource;
    private Proxy proxy;

    /**
     * 构造函数
     * 
     * @author 刘太信
     * @date 2017年8月15日 下午6:49:27
     * @param proxySource
     *            http数据源
     */
    public HttpProxyPolicy(HttpProxySource proxySource) {
        this.proxySource = proxySource;
    }

    @Override
    public AtomicBoolean isChanging() {
        return isChanging;
    }

    @Override
    public Proxy createProxy() {
        int time = 0;
        while (time++ < retryTime) {
            try {
                proxy = proxySource.get();
                if (null != proxy ) {
                    LogUtil.info(String.format("代理切换成功，代理ip：%s，代理port:%d", proxy.getHost(), proxy.getPort()));
                    break;
                }
            } catch (Exception e) {
                LogUtil.error(String.format("第 %d 切换代理异常", time), e);
            }
        }
        return proxy;
    }
    
    @Override
    public Proxy getCurrentProxy() {
        return proxy;
    }
    
    
    @Override
    public ReentrantLock getReentrantLock() {
        return reentrantLock;
    }

    @Override
    public void setIsChanging(Boolean flag) {
        isChanging.set(flag);
    }

}
