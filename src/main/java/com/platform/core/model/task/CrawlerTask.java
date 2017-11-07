package com.platform.core.model.task;

import java.util.List;

import com.platform.core.model.AbstractBaseModel;
import com.platform.core.model.processor.ProcessorRule;
import org.assertj.core.util.Arrays;

import com.platform.core.model.downloader.DownloaderRule;

import lombok.Getter;
import lombok.Setter;

/**
 * 爬取任务
 * 
 * @author 王龙
 * @date 2017年10月26日 下午5:09:06
 */

@Getter
@Setter
public class CrawlerTask extends AbstractBaseModel {

    private static final long serialVersionUID = 1L;

    /** 任务名称 */
    private String name;

    /** 创建人 */
    private String creator;

    /** 解析规则 */
    private List<ProcessorRule> processorRuleList;

    /** 下载规则 */
    private List<DownloaderRule> downloaderRuleList;

    /** 起始url */
    private String[] startUrl;

    /**
     * @param args
     */
    public void setStartUrl(String[] args) {
        startUrl = Arrays.array(args);
    }

}
