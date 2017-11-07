package com.platform.core.model.downloader;

import com.platform.core.contants.DownloaderType;
import com.platform.webmagic.download.proxy.policy.ProxyPolicy;

import lombok.Getter;
import lombok.Setter;
import us.codecraft.webmagic.downloader.HttpUriRequestConverter;

@Getter
@Setter
public class HttpClientDownloaderRule implements DownloaderRule {

    private String rule;

    private int thread = 3;

    private ProxyPolicy proxyPolicy;

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();

    private final DownloaderType type = DownloaderType.DOWNLOADER_COMMON;
}
