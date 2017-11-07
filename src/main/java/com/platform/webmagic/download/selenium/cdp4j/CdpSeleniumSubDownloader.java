package com.platform.webmagic.download.selenium.cdp4j;

import com.platform.webmagic.download.SubDownloader;
import com.platform.webmagic.download.proxy.policy.ProxyPolicy;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;

import java.util.regex.Pattern;

public class CdpSeleniumSubDownloader  extends CdpSeleniumDownloader implements SubDownloader {
    private ProxyPolicy proxyPolicy;

    @Override
    public void changeProxyIfNotAdslProxy(Proxy proxy) {
        sessionQueue.add(SessionFactoryManager.getFactory(proxy.getHost()+":"+proxy.getPort()).create());
    }

    @Override
    public Page download(Request request, Task task) {
        while(!proxyPolicy.isChanging().get()){
            return super.download(request, task);
        }
        return Page.fail();
    }

    @Override
    public void onSuccess(Request request) {

    }

    @Override
    public void onError(Request request) {
        changeProxy();
    }

    @Override
    public ProxyPolicy getProxyPolicy() {
        return proxyPolicy;
    }

    @Override
    public Pattern getPattern() {
        return null;
    }
}
