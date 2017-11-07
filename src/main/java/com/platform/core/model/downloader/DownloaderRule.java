package com.platform.core.model.downloader;

import java.util.regex.Pattern;

import com.platform.core.contants.DownloaderType;

/**
 * downloader正则接口
 * 
 * @author 王龙
 * @date 2017年10月30日 下午2:19:08
 */
public interface DownloaderRule {
    /**
     * 获取Pattern对象
     * 
     * @description
     * @author 王龙
     * @date 2017年10月30日 下午2:19:08
     * @return
     */
    default Pattern getPattern() {
        return Pattern.compile(getRule());
    }

    /**
     * 获取正则表达式
     * 
     * @description
     * @author 王龙
     * @date 2017年10月31日 下午3:22:33
     * @return
     */
    public String getRule();
    
    /**
     * 获取downloader的线程数
     * 
     * @description
     * @author 王龙
     * @date 2017年10月31日 下午3:34:36
     * @return
     */
    public int getThread();
    public void setThread(int threadNum);

    /**
     * 获取IP的代理策略
     * 
     * @description
     * @author 王龙
     * @date 2017年10月31日 下午3:34:48
     * @return
     */
   // public ProxyPolicy getProxyPolicy();

    /**
     * 获取Downloader类型名称
     * 
     * @description
     * @author 王龙
     * @date 2017年10月26日 下午5:00:10
     * @return
     */
    DownloaderType getType();
}
