package com.platform.webmagic.download;

import com.platform.webmagic.download.proxy.policy.AdslProxyPolicy;
import com.platform.webmagic.download.proxy.policy.ProxyPolicy;
import com.platform.core.model.downloader.DownloaderRule;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.proxy.Proxy;

/**
 * 多下载器接口
 *
 * @author 刘太信
 * @description 提供onError()和onSuccess()接口
 * @date 2017年8月10日 下午5:48:21
 */
public interface SubDownloader extends Downloader, PatternMatcher {

    default void initSuper(DownloaderRule rule) {
        setThread(rule.getThread());
        changeProxy();
    }

    /**
     * 切换代理
     *
     * @description
     * @author 王龙
     * @date 2017年11月1日 下午1:41:51
     */
    default void changeProxy() {
        if (null != getProxyPolicy()) {
            // 切换代理
            Proxy proxy = getProxyPolicy().getNewProxy();
            // adsl模式直接再次运行
            if (!(getProxyPolicy() instanceof AdslProxyPolicy)) {
                // http代理执行，adsl模式直接再次运行
                if (null != proxy) {
                    changeProxyIfNotAdslProxy(proxy);
                }
            }
        }
    }

    void changeProxyIfNotAdslProxy(Proxy proxy);

    void onSuccess(Request request);

    void onError(Request request);

    ProxyPolicy getProxyPolicy();
}