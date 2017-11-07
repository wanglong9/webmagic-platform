package com.platform.test.spider.task;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.platform.core.model.downloader.DownloaderRule;
import com.platform.core.model.processor.ProcessorRule;
import com.platform.webmagic.download.CompositeDownloader;
import com.platform.webmagic.processor.CompositePageProcessor;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

public class WuBaTest {
    WuBaTask task = new WuBaTask();

    /**
     * 测试详情页面
     * 
     * @description
     * @author 王龙
     * @date 2017年10月25日 下午2:56:10
     */
    @Test
    public void testDetailRule() {
        List<ProcessorRule> processorRuleList = new ArrayList<>();
        List<DownloaderRule> downloaderRuleList = new ArrayList<>();
        processorRuleList.add(task.getDetailRule());
        processorRuleList.add(task.getExtraFieldRule());
        downloaderRuleList.add(task.getSeleniumDownloaderRule());
        downloaderRuleList.add(task.getHttpClientDownloaderRule());
        run(processorRuleList, downloaderRuleList, "58租房详情测试","http://hz.58.com/xihuqu/chuzu/","http://hz.58.com/zufang/31682192068661x.shtml" );
    }

    /**
     * 测试追加房租列表
     * 
     * @description
     * @author 王龙
     * @date 2017年10月25日 下午2:56:18
     */
    @Test
    public void testExtraFieldListRule() {
        List<ProcessorRule> processorRuleList = new ArrayList<>();
        processorRuleList.add(task.getExtraFieldRule());
        run(processorRuleList, "http://hz.58.com/xihujingqu/chuzu/", "58追加房租列表测试");
    }

    /**
     * 测试追加商圈url
     * 
     * @description
     * @author 王龙
     * @date 2017年10月25日 下午2:56:28
     */
    @Test
    public void testBusinessRule() {
        List<ProcessorRule> processorRuleList = new ArrayList<>();
        processorRuleList.add(task.getBusinessRule());
        run(processorRuleList, "http://hz.58.com/xihuqu/chuzu/", "58追加商圈测试");
    }

    /**
     * 测试追加城区url
     * 
     * @description
     * @author 王龙
     * @date 2017年10月25日 下午2:56:39
     */
    @Test
    public void testDistrictRule() {
        List<ProcessorRule> processorRuleList = new ArrayList<>();
        processorRuleList.add(task.getDistrictRule());
        run(processorRuleList, "http://hz.58.com/chuzu/", "58追加城区测试");
    }

    @Test
    public void testWhole() {
        List<ProcessorRule> processorRuleList = new ArrayList<>();
        processorRuleList.add(task.getDistrictRule());
        processorRuleList.add(task.getBusinessRule());
        processorRuleList.add(task.getExtraFieldRule());
        processorRuleList.add(task.getDetailRule());
        run(processorRuleList, "http://hz.58.com/chuzu/", "58租房");
    }

    private void run(List<ProcessorRule> processorRuleList, String startUrl, String taskName) {
        task.setName("58同城");
        task.setProcessorRuleList(processorRuleList);
        task.setStartUrl(new String[]{startUrl});

        CompositePageProcessor processor = new CompositePageProcessor(task.getProcessorRuleList());
        Spider spider = new Spider(processor);
        spider.addPipeline(new ConsolePipeline()).addUrl(startUrl).thread(1).run();
    }

    private void run(List<ProcessorRule> processorRuleList, List<DownloaderRule> downloaderRuleList, String taskName,
            String ... startUrl) {
        task.setName("58同城");
        task.setProcessorRuleList(processorRuleList);
        task.setStartUrl(startUrl);

        CompositePageProcessor processor = new CompositePageProcessor(task.getProcessorRuleList());
        CompositeDownloader downloader = new CompositeDownloader(downloaderRuleList);

        Spider spider = new Spider(processor);
        spider.setDownloader(downloader);
        spider.setUUID(taskName);
        spider.addPipeline(new ConsolePipeline()).addUrl(startUrl).thread(1).run();
    }
}
