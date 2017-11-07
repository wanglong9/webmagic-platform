package com.platform.webmagic.download.selenium.cdp4j;

import com.platform.webmagic.Callback;
import io.webfolder.cdp.session.Session;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 利用Chrome DevTools Protocol解析js页面，可实现无窗口模式，性能优于phantomjs
 *
 * @author 王龙
 */
public class CdpSeleniumDownloader extends AbstractDownloader {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * session容器
     */
    BlockingDeque<Session> sessionQueue;

    /**
     * 页面加载超时时间
     */
    protected Integer pageLoadTimeout = 30;

    /**
     * session数
     */
    private int threadNum = 5;

    /**
     * 下载完页面后需要做一些的处理，如等待某个元素的加载完毕，或者滚动滚动条，注入一段js代码等
     */
    protected Callback<Session> waitDownloadComplete;

    /**
     * 构造函数
     *
     * @param waitDownloadComplete 设置下载完页面后需要做一些的处理，可根据url进行正则表达式做相应的处理
     * @author 刘太信
     * @date 2017年8月18日 下午9:31:37
     */
    public CdpSeleniumDownloader(Callback<Session> waitDownloadComplete) {
        this.waitDownloadComplete = waitDownloadComplete;
    }

    public CdpSeleniumDownloader() {
        sessionQueue = new LinkedBlockingDeque<>(threadNum);
        init();
    }

    /**
     * 设置页面加载时间
     *
     * @param pageLoadTimeout 页面加载时间
     * @return 自身实例
     * @author 刘太信
     * @date 2017年8月18日 下午9:33:51
     */
    public void setPageLoadTimeout(Integer pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    protected void init() {
        while (--threadNum > 0) {
            Session session = SessionFactoryManager.getFactory().create();
            sessionQueue.add(session);
        }
    }

    @Override
    public Page download(Request request, Task task) {
        if (task == null || task.getSite() == null) {
            throw new NullPointerException("task or site can not be null");
        }
        Session session = sessionQueue.poll();
        Page page = Page.fail();
        try {
            session.navigate(request.getUrl());
            session.waitDocumentReady(pageLoadTimeout);
            // wait policy
            if (null != waitDownloadComplete) {
                waitDownloadComplete.execute(session);
            }
            String content = (String) session.getProperty("//body", "outerText");
            page = handleResponse(request, content);
            onSuccess(request);
        } catch (Exception e) {
            onError(request);
        } finally {
            sessionQueue.push(session);
        }
        return page;
    }

    private Page handleResponse(Request request, String content) {
        Page page = new Page();
        page.setRawText(content);
        page.setStatusCode(getResponseCode(request.getUrl()));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setDownloadSuccess(true);
        return page;
    }

    protected int getResponseCode(String url) {
        try {
            HttpResponse response = org.apache.http.client.fluent.Request.Get(url).connectTimeout(3000).execute()
                    .returnResponse();
            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setThread(int threadNum) {
        this.threadNum = threadNum;
        sessionQueue = new LinkedBlockingDeque<>(threadNum);
    }
}
