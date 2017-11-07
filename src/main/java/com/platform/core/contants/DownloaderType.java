package com.platform.core.contants;

/** 
 * DownloaderType 下载类型
 * @author 王龙 
 * @date 2017年10月30日 下午2:34:43  
 */
public enum DownloaderType {
    DOWNLOADER_JS("js渲染页面", 1), DOWNLOADER_COMMON("普通页面", 2);

    // 构造方法
    private DownloaderType(String type, int index) {
    }
}
