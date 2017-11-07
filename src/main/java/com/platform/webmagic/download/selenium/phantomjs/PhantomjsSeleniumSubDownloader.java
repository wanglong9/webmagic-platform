package com.platform.webmagic.download.selenium.phantomjs;

import java.util.regex.Pattern;

import com.platform.core.model.downloader.SeleniumDownloaderRule;
import com.platform.util.LogUtil;
import com.platform.webmagic.download.SubDownloader;
import com.platform.webmagic.download.proxy.policy.ProxyPolicy;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.proxy.Proxy;

/**
 * SeleniumDownloader的多download共存类，基于SeleniumDownloader
 *
 * @author 刘太信
 * @date 2017年8月18日 下午9:39:15
 */
public class PhantomjsSeleniumSubDownloader extends PhantomjsSeleniumDownloader implements SubDownloader {
    private SeleniumDownloaderRule rule;
    private ProxyPolicy proxyPolicy;

    public PhantomjsSeleniumSubDownloader(SeleniumDownloaderRule rule) {
        this.rule = rule;
        this.proxyPolicy = rule.getProxyPolicy();
        initSuper(rule);
    }

    @Override
    public void onError(Request request) {
        // 添加处理逻辑
        LogUtil.debug("downloader异常错误日志：" + request.getUrl());
        changeProxy();
    }

    @Override
    public void onSuccess(Request request) {
        LogUtil.info("downloader成功:" + request.getUrl());
    }

    @Override
    public Pattern getPattern() {
        return rule.getPattern();
    }

    @Override
    public void changeProxyIfNotAdslProxy(Proxy proxy) {
        webDriverPool.changeProxy(proxy);
    }

    @Override
    public ProxyPolicy getProxyPolicy() {
        return proxyPolicy;
    }
}
