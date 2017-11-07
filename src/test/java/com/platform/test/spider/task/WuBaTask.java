package com.platform.test.spider.task;

import java.util.HashMap;
import java.util.Map;

import com.platform.core.model.downloader.HttpClientDownloaderRule;
import com.platform.core.model.downloader.SeleniumDownloaderRule;
import com.platform.core.model.processor.detail.DetailProcessorRule;
import com.platform.core.model.processor.list.ExtraFieldListProcessorRule;
import com.platform.core.model.task.CrawlerTask;
import com.platform.core.model.processor.list.CommonListProcessorRule;
import com.platform.core.model.processor.list.ListProcessorRule;

public class WuBaTask extends CrawlerTask {

    /** TODO */
    private static final long serialVersionUID = 1L;

    public ListProcessorRule getDistrictRule() {
        CommonListProcessorRule districtRule = new CommonListProcessorRule();
        districtRule.setMatchRule(
                "html.xpath (\"*//dl[@class='secitem secitem_fist']\").match()&&!html.xpath(\"*//div[@class='arealist']\").match()");
        districtRule.setUrlsRule("html.xpath(\"*//dl[@class='secitem secitem_fist']\").links().all()");
        return districtRule;
    }

    public ListProcessorRule getBusinessRule() {
        CommonListProcessorRule businessRule = new CommonListProcessorRule();
        businessRule.setMatchRule(
                "html.xpath (\"*//div[@class=\'arealist\']\").match()&&!html.xpath(\"*//div[@class='arealist']//a[@class='select']\").match()");
        businessRule.setUrlsRule("html.xpath (\"*//div[@class=\'arealist\']\").links().all()");
        return businessRule;
    }

    public ListProcessorRule getExtraFieldRule() {
        ExtraFieldListProcessorRule listRule = new ExtraFieldListProcessorRule();
        listRule.setMatchRule("html.xpath(\"*//div[@class='arealist']//a[@class='select']\").match()");
        listRule.setUrlsRule("html.xpath(\"*//ul[@class='listUl']/li\").nodes ()");
        listRule.setUrlParamRule("html.xpath(\"//li//div[@class='des']/h2/a/@href\").regex('^[^|||]+').get()");
//      Map<String, Object> paramRule = new HashMap<>();
//      paramRule.put("户型", "html.xpath(\"//p[@class='room']/text()\").get()");
//      listRule.setParamRule(paramRule);
        listRule.setNextPageRule("html.xpath(\"*//li[@id='bottom_ad_li']/div[@class='pager']/a[@class='next']/@href\").get()");
        return listRule;
    }

    public DetailProcessorRule getDetailRule() {
        DetailProcessorRule houseRule = new DetailProcessorRule();
        houseRule.setMatchRule("url.regex (\"http://short.58.com.*|http://jxjump.58.com.*|(.*.shtml.*)\").match()");
        Map<String, String> map = new HashMap<>();
        //map.put("title", "html.xpath(\"*//div[@class=\'house-title\']/h1/text()\").get()");
        map.put("html", "html.get()");
        houseRule.setAnalysisRule(map);
        return houseRule;
    }
    
    public HttpClientDownloaderRule getHttpClientDownloaderRule() {
        HttpClientDownloaderRule rule= new HttpClientDownloaderRule();
        //rule.setProxyPolicy();
        rule.setRule("");
        rule.setThread(3);
        return rule;
    }
    
    public SeleniumDownloaderRule getSeleniumDownloaderRule() {
        SeleniumDownloaderRule rule= new SeleniumDownloaderRule();
        //rule.setProxyPolicy();
        rule.setRule("http://short.58.com.*|http://jxjump.58.com.*|(.*.shtml.*)");
        rule.setThread(2);
        return rule;
    }
}
