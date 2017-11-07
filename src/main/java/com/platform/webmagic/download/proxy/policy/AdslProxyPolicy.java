package com.platform.webmagic.download.proxy.policy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import com.platform.util.LogUtil;
import com.platform.utils.ProxyUtils;

import us.codecraft.webmagic.proxy.Proxy;

/**
 * ADSL代理
 *
 * @author 王龙
 * @date 2017年11月1日 下午3:00:14
 */
public class AdslProxyPolicy implements ProxyPolicy {
    protected AtomicBoolean isChanging = new AtomicBoolean(false);
    protected ReentrantLock reentrantLock = new ReentrantLock();
    
    @Override
    public AtomicBoolean isChanging() {
        return isChanging;
    }
    
    @Override
    public Proxy createProxy() {
        int time = 0;
        String oldIP = ProxyUtils.getV4IP();
        do {
            restartAdslAction();
        } while (ProxyUtils.getV4IP().equals(oldIP) && ++time < 3);
        return null;
    }
    
    @Override
    public Proxy getCurrentProxy() {
        return null;
    }
    
    /**
     * 异步重启ADSL
     */
    private void restartAdslAction() {
        LogUtil.warn("切换adsl------ing");
        String adslStop = "sudo adsl-stop";
        String stop = executeCommand(adslStop);
        LogUtil.warn("断开ADSL..." + stop);
        String adslStart = "sudo adsl-start";
        String start = executeCommand(adslStart);
        LogUtil.warn("连接ADSL..." + start);
    }
    
    /**
     * 执行shell命令
     *
     * @param command
     *
     * @return
     */
    private String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
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
