package com.platform.webmagic.download.http;

import java.util.Arrays;
import java.util.regex.Pattern;

import com.platform.core.model.downloader.HttpClientDownloaderRule;
import com.platform.core.model.downloader.DownloaderRule;
import com.platform.utils.LogUtil;
import com.platform.webmagic.download.SubDownloader;
import com.platform.webmagic.download.proxy.policy.ProxyPolicy;

import us.codecraft.webmagic.utils.ProxyUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * HttpClientSubDownloader的多download共存类，基于httpClientdownload
 *
 * @author 王龙
 * @date 2017年8月10日 下午5:53:17
 */
public class HttpClientSubDownloader extends HttpClientDownloader implements SubDownloader {


    private HttpClientDownloaderRule rule;

    private ProxyPolicy proxyPolicy;

    public HttpClientSubDownloader(HttpClientDownloaderRule rule) {
        this.rule = rule;
        this.proxyPolicy = rule.getProxyPolicy();
        initSuper(rule);
    }

    @Override
    public void initSuper(DownloaderRule rule) {
        //初始化HttpClientDownloader的属性
        setProxyProvider(new SimpleProxyProvider(Arrays.asList(getProxyPolicy().getNewProxy())));
        setThread(rule.getThread());
    }

    /**
     * 一般网络异常、IO流异常时，进入该方法
     * * @param request
     */
    @Override
    public void onError(Request request) {
        int cycleRetryTimes = getCycleRetryTimes(request);
        if (cycleRetryTimes == 0 && ! ProxyUtils.validateProxy(getProxyPolicy().getCurrentProxy())) {
            //更新代理
            changeProxy();
            setCycleRetryTimes(request);
        }
    }

    /**
     * 重新给request赋值cycle次数
     *
     * @param request
     */
    private void setCycleRetryTimes(Request request) {
        request.setPriority(0).putExtra(Request.CYCLE_TRIED_TIMES, 3);
    }

    /**
     * 获取当前的循环次数
     *
     * @param request
     * @return
     */
    private int getCycleRetryTimes(Request request) {
        int cycleTriedTimes;
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            cycleTriedTimes = 0;
        } else {
            cycleTriedTimes = (Integer) cycleTriedTimesObject;
        }
        return cycleTriedTimes;
    }

    /**
     * 表示数据成功，需要自行根据statuCode进行处理
     *
     * @param request
     */
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
        setProxyProvider(new SimpleProxyProvider(Arrays.asList(proxy)));
    }

    @Override
    public ProxyPolicy getProxyPolicy() {
        return proxyPolicy;
    }
}
