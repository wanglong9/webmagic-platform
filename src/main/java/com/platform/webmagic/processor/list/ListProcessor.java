package com.platform.webmagic.processor.list;

import com.platform.core.contants.CrawlerContants;
import org.apache.commons.lang3.StringUtils;

import com.platform.core.model.processor.list.ListProcessorRule;
import com.platform.utils.JexlUtils;
import com.platform.webmagic.processor.RulePageProcessor;

import us.codecraft.webmagic.Page;

/**
 * 列表页面解析规则
 *
 * @author 王龙
 * @date 2017-10-23 14：00
 */
public abstract class ListProcessor implements RulePageProcessor {

    public abstract ListProcessorRule getRule();

    /**
     * 获取【下一页】按钮的url
     * 
     * @description
     * @author 王龙
     * @date 2017年10月26日 下午5:50:31
     * @param page
     * @return
     */
    protected String getNextPageUrl(Page page) {
        if (StringUtils.isNotEmpty(getRule().getNextPageRule())) {
            return JexlUtils.jexlResult(page.getHtml(), CrawlerContants.MATCH_HTML, getRule().getNextPageRule());
        }
        return null;
    }

    @Override
    public void processPage(Page page) {
        // 追加下一页url
        page.addTargetRequest(getNextPageUrl(page));
    }
}
