package com.platform.core.model.downloader;

import com.platform.core.contants.DownloaderType;
import com.platform.webmagic.download.proxy.policy.ProxyPolicy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeleniumDownloaderRule implements DownloaderRule{
    
    private String rule;

    private int thread = 3;

    private ProxyPolicy proxyPolicy;
    
    private final DownloaderType type = DownloaderType.DOWNLOADER_JS;

}
