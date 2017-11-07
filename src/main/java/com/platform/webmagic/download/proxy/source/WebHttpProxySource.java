package com.platform.webmagic.download.proxy.source;

import us.codecraft.webmagic.proxy.Proxy;

/**
 * 网络接口数据源
 *
 * @author 刘太信
 * @date 2017年8月18日 下午9:04:32
 */
public class WebHttpProxySource implements HttpProxySource {

    @Override
    public Proxy get() {
        //web 接口http代理自行实现
        return null;
    }

}