package com.platform.webmagic.download.selenium.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.platform.util.LogUtil;
import com.platform.util.PropertiesUtil;
import com.platform.webmagic.Callback;

import us.codecraft.webmagic.proxy.Proxy;

/**
 * webDrive池，1个phantomjs进程只能提供1个页面访问，不支持并发，但可以通过开启多个进程和分布式部署解决。<br/>
 * <ul>
 * <ol>
 * <b>使用webdriver池的理由:</b>
 * webDrive使用完毕就关闭进程时，程序会耗费大量时间进行创建进程关闭进程。故采用页面使用完就访问webdrive池进行复用
 * </ol>
 * <ol>
 * <b>注意1：</b>1个phantomjs进程占用内存50mb左右，请依据自身内存环境进行设置。
 * </ol>
 * <ol>
 * <b>注意2：</b>
 * 当采用webdrive池时，直接关闭进程如：eclipse的强制关闭、linux的kill进程命令都无法触发addShutdownHook钩子进行进程关闭
 * ，此时需要手动清除 phantomjs进程。
 * </ol>
 * </ul>
 *
 * @author 刘太信
 * @date 2017年7月29日 下午7:50:50
 */
public class WebDriverPool {
    /**
     * 关闭图片加载 关闭ssl错误
     */
    protected final static String[] cliArgsCap = new String[]{"--load-images=no", "--ignore-ssl-errors=yes"};

    /**
     * 默认池大小
     */
    protected int poolSize = 3;
    /**
     * 池容器
     */
    protected BlockingDeque<PhantomJSDriver> innerQueue = new LinkedBlockingDeque<PhantomJSDriver>(poolSize);
    /**
     * 记录当前使用的数量
     */
    protected AtomicInteger refCount = new AtomicInteger(0);
    /**
     * 锁
     */
    protected ReentrantLock reentrantLock = new ReentrantLock();
    /**
     * 从池中获取webdriver的条件
     */
    protected Condition condition = reentrantLock.newCondition();
    /**
     * 暂停获取并等待标识
     */
    protected AtomicBoolean awaitFlag = new AtomicBoolean(false);
    /**
     * webdriver全局配置
     */
    protected static DesiredCapabilities caps = DesiredCapabilities.phantomjs();
    /**
     * 配置文件中的key
     */
    protected final static String PHANTOMJS_PATH_KEY = "phantomjs.path";

    static {
        PropertiesConfiguration config = PropertiesUtil.parseFile("phantomjs.properties");
        if (null == config || !config.containsKey(PHANTOMJS_PATH_KEY)) {
            LogUtil.error("缺少phantomjs.properties配置文件");
            throw new RuntimeException("缺少phantomjs.properties配置文件");
        }
        String phantomjs_path = (String) config.getProperty(PHANTOMJS_PATH_KEY);
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs_path);
        // 截图，需要验证某个元素的状态或者显示的数值时，可以将屏幕截取下来进行对比；或者在异常或者错误发生的时候将屏幕截取并保存起来，供后续分析和调试所用
        caps.setCapability(CapabilityType.TAKES_SCREENSHOT, false);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
    }

    /**
     * 构造函数
     *
     * @author 刘太信
     * @date 2017年8月18日 下午9:07:20
     */
    public WebDriverPool() {
        addCloseProcessHook();
    }

    /**
     * 从池中获取一个webdriver
     *
     * @param pageLoadTimeout 页面加载超时时间
     * @param capsCallback    自定义配置，如自定义header等
     * @return webdriver
     * @throws InterruptedException
     * @author 刘太信
     * @date 2017年8月18日 下午9:07:24
     */
    public PhantomJSDriver get(Integer pageLoadTimeout, Callback<DesiredCapabilities> capsCallback)
            throws InterruptedException {
        if (refCount.get() >= poolSize || awaitFlag.get()) {
            try {
                reentrantLock.lock();
                while (refCount.get() >= poolSize || awaitFlag.get()) {
                    condition.await();
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        PhantomJSDriver poll = innerQueue.poll();
        if (poll != null) {
            return poll;
        }

        refCount.incrementAndGet();
        if (null != capsCallback) {
            capsCallback.execute(caps);
        }
        PhantomJSDriver mDriver = new PhantomJSDriver(caps);
        mDriver.manage().timeouts().pageLoadTimeout(null == pageLoadTimeout ? 60 : pageLoadTimeout, TimeUnit.SECONDS);
        innerQueue.add(mDriver);

        return innerQueue.take();
    }

    /**
     * 设置池容量
     *
     * @param poolSize
     * @author 刘太信
     * @date 2017年8月18日 下午9:08:25
     */
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
        innerQueue = new LinkedBlockingDeque<PhantomJSDriver>(poolSize);
    }

    /**
     * 获取当前是否处于暂停并等待状态
     *
     * @return 暂停并等待为true，否则false
     * @author 刘太信
     * @date 2017年8月18日 下午9:08:40
     */
    public boolean getAwaitFlag() {
        return awaitFlag.get();
    }

    /**
     * 释放webdriver返回到池中
     *
     * @param webDriver
     * @author 刘太信
     * @date 2017年8月18日 下午9:09:15
     */
    public void returnToPool(PhantomJSDriver webDriver) {
        // webDriver.quit();
        // webDriver=null;
        refCount.decrementAndGet();
        innerQueue.add(webDriver);
    }

    /**
     * 关闭释放资源，不推荐手动调用，建议使用returnToPool
     *
     * @param webDriver
     * @author 刘太信
     * @date 2017年8月18日 下午9:09:29
     */
    public void close(PhantomJSDriver webDriver) {
        refCount.decrementAndGet();
        if (innerQueue.contains(webDriver)) {
            innerQueue.remove(webDriver);
        }
        webDriver.close();
        webDriver.quit();
        webDriver = null;
    }

    /**
     * 关闭webdriver池，释放所有资源
     *
     * @author 刘太信
     * @date 2017年8月18日 下午9:09:40
     */
    public void shutdown() {
        try {
            for (PhantomJSDriver driver : innerQueue) {
                close(driver);
            }
            innerQueue.clear();
        } catch (Exception e) {
            LogUtil.error("webdriverpool关闭失败", e);
        }
    }

    /**
     * 系统退出自动关闭，eclipse的强制关闭、linux的kill进程命令无法触发此方法，需手动关闭进程
     *
     * @author 刘太信
     * @date 2017年8月18日 下午9:10:00
     */
    protected void addCloseProcessHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                LogUtil.info("关闭phantomjs进程");
                shutdown();
            }
        }));
    }

    /**
     * 切换代理，原理为：等待所有任务结束后，修改全局配置，之后关闭池重新创建webdriver
     *
     * @param proxy
     * @author 刘太信
     * @date 2017年8月18日 下午9:11:28
     */
    public void changeProxy(Proxy proxy) {
        if (awaitFlag.compareAndSet(false, true)) {
            List<String> newCliArgsCap = new ArrayList<>();
            newCliArgsCap.addAll(Arrays.asList(cliArgsCap));
            if (null != proxy) {
                newCliArgsCap.add(String.format("--proxy=%s:%d", proxy.getHost(), proxy.getPort()));
                if (!StringUtils.isEmpty(proxy.getUsername()) && !StringUtils.isEmpty(proxy.getPassword())) {
                    newCliArgsCap.add(String.format("--proxy-auth=%s:%s", proxy.getUsername(), proxy.getPassword()));
                }
                newCliArgsCap.add("--proxy-type=http");
            }
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, Collections.unmodifiableList(newCliArgsCap));

            // 等待所有创建的webdriver都执行完毕后切换
            while (refCount.get() > innerQueue.size()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
            }

            // 关闭已创建的用代理重新创建
            shutdown();

            try {
                reentrantLock.lock();
                awaitFlag.set(false);
                condition.signalAll();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /**
     * 池中所有的webdriver是否都已经下载完毕，用于adsl切换代理
     *
     * @author 刘太信
     * @date 2017年8月18日 下午9:12:19
     */
    public void ready() {
        if (awaitFlag.compareAndSet(false, true)) {
            // 等待所有创建的webdriver都执行完毕后切换
            while (refCount.get() > innerQueue.size()) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                }
            }

            try {
                reentrantLock.lock();
                awaitFlag.set(false);
                condition.signalAll();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}