package com.platform.webmagic.download.selenium.phantomjs;

import java.util.Map;
import java.util.Map.Entry;

import com.platform.webmagic.download.selenium.tool.WebDriverPool;
import org.apache.http.HttpResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.platform.util.LogUtil;
import com.platform.webmagic.Callback;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;

/**
 * Selenium+phantomjs解决ajax动态页面、iframe动态页面、302跳转页面下载，还原最终呈现给用户的界面进行下载
 * <p>
 * 注意：
 * </p>
 * <ol>
 * <li>代理请尽量使用adsl切换ip代理策略</li>
 * <li>http代理策略速度很慢，请尽量使用收费代理，否则超时严重</li>
 * </ol>
 *
 * @author 刘太信
 * @date 2017年8月10日 下午5:50:08
 */
public class PhantomjsSeleniumDownloader extends AbstractDownloader {
    /**
     * webdriver池
     */
    protected WebDriverPool webDriverPool = new WebDriverPool();
    /**
     * 下载完页面后需要做一些的处理，如等待某个元素的加载完毕，或者滚动滚动条，注入一段js代码等
     */
    protected Callback<PhantomJSDriver> waitDownloadComplete;
    /**
     * 页面加载超时时间
     */
    protected Integer pageLoadTimeout = 10;

    /**
     * 构造函数
     *
     * @author 刘太信
     * @date 2017年8月18日 下午9:31:32
     */
    public PhantomjsSeleniumDownloader() {
    }

    /**
     * 构造函数
     *
     * @param waitDownloadComplete 设置下载完页面后需要做一些的处理，可根据url进行正则表达式做相应的处理
     * @author 刘太信
     * @date 2017年8月18日 下午9:31:37
     */
    public PhantomjsSeleniumDownloader(Callback<PhantomJSDriver> waitDownloadComplete) {
        this.waitDownloadComplete = waitDownloadComplete;
    }

    /**
     * 设置页面加载时间
     *
     * @param pageLoadTimeout 页面加载时间
     * @return 自身实例
     * @author 刘太信
     * @date 2017年8月18日 下午9:33:51
     */
    public PhantomjsSeleniumDownloader pageLoadTimeout(Integer pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
        return this;
    }

    /**
     * 获取statuscode，由于Selenium并未实现
     *
     * @param url 网址
     * @return 状态码
     * @author 刘太信
     * @date 2017年8月18日 下午9:38:17
     */
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
    public Page download(Request request, Task task) {
        PhantomJSDriver driver = null;
        Page page = Page.fail();
        try {
            driver = webDriverPool.get(pageLoadTimeout, new Callback<DesiredCapabilities>() {
                @Override
                public boolean execute(DesiredCapabilities obj) {
                    if (null != task && null != task.getSite() && null != task.getSite().getHeaders()) {
                        for (Entry<String, String> header : task.getSite().getHeaders().entrySet()) {
                            obj.setCapability(
                                    PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + header.getKey(),
                                    header.getValue());
                        }
                    }
                    return false;
                }
            });

            WebDriver.Options manage = driver.manage();
            if (null != task && null != task.getSite() && task.getSite().getCookies() != null) {
                for (Map.Entry<String, String> cookieEntry : task.getSite().getCookies().entrySet()) {
                    Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
                    manage.addCookie(cookie);
                }
            }
            driver.get(request.getUrl());
            manage.window().maximize();
            // wait policy
            if (null != waitDownloadComplete) {
                waitDownloadComplete.execute(driver);
            }

            WebElement webElement = driver.findElement(By.xpath("/html"));
            String content = webElement.getAttribute("outerHTML");
            page.setRawText(content);
            page.setBytes(content.getBytes());
            // page.setRawText(UrlUtils.canonicalizeUrl(content,request.getUrl()); //
            // fixAllRelativeHrefs 修复相对路径

            page.setStatusCode(getResponseCode(request.getUrl()));
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
            page.setDownloadSuccess(true);
            onSuccess(request);
            LogUtil.info(String.format("downloading page with web driver success %s", request.getUrl()));
        } catch (Exception e) {
            LogUtil.warn(String.format("download page with web driver %s error", request.getUrl()), e);
            onError(request);
        } finally {
            if (null != driver) {
                webDriverPool.returnToPool(driver);
            }
        }
        return page;
    }

    @Override
    public void setThread(int threadNum) {
        webDriverPool.setPoolSize(threadNum);
    }

    /**
     * 检测js池是否准备完成
     *
     * @description
     * @author 王龙
     * @date 2017年11月1日 下午2:23:20
     */
    public void ready() {
        webDriverPool.ready();
    }
}