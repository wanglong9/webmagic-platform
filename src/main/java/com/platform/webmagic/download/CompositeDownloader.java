package com.platform.webmagic.download;

import java.util.ArrayList;
import java.util.List;

import com.platform.core.model.downloader.HttpClientDownloaderRule;
import com.platform.core.model.downloader.SeleniumDownloaderRule;
import com.platform.webmagic.download.http.HttpClientSubDownloader;
import com.platform.core.model.downloader.DownloaderRule;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * 混合下载器，解封spider只能设置一个downloader的弊端，根据url进行动态切换下载器下载内容
 * 
 * @author 王龙
 * @date 2017年10月31日 上午10:51:28
 */
public class CompositeDownloader implements Downloader {
    /** 下载器贮存类 */
    private List<SubDownloader> subDownloaders;

    private int threadNum = 0;

    HttpClientDownloader downloader = new HttpClientDownloader();

    public CompositeDownloader(List<DownloaderRule> ruleList) {
        this.subDownloaders = getPageDownloader(ruleList);
    }

    @Override
    public Page download(Request request, Task task) {
        Page page = Page.fail();
        for (SubDownloader subDownloader : subDownloaders) {
            if (subDownloader.match(request)) {
                page = subDownloader.download(request, task);
                return page;
            }
        }
        // 无配置或未找到匹配时使用HttpClientDownloader
        page = downloader.download(request, task);
        return page;
    }

    private List<SubDownloader> getPageDownloader(List<DownloaderRule> ruleList) {
        subDownloaders = new ArrayList<>();
        ruleList.forEach(rule -> {
            if (threadNum != 0) {
                rule.setThread(threadNum);
            }
            SubDownloader subDownloader = null;
            switch (rule.getType()) {
            case DOWNLOADER_COMMON:
                subDownloader = new HttpClientSubDownloader((HttpClientDownloaderRule) rule);
                break;
            case DOWNLOADER_JS:
                subDownloader = new com.platform.webmagic.download.selenium.phantomjs.PhantomjsSeleniumSubDownloader((SeleniumDownloaderRule) rule);
                break;
            default:
                break;
            }
            subDownloaders.add(subDownloader);
        });
        return subDownloaders;
    }

    /**
     * 优先级大于DownloaderRule,如果设置值将统一设置所有SubDownloader的线程数，建议不使用
     */
    @Override
    @Deprecated
    public void setThread(int threadNum) {
        this.threadNum = threadNum;
    }
}
