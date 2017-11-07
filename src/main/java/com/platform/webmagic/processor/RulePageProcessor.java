package com.platform.webmagic.processor;

import com.platform.core.contants.CrawlerContants;
import com.platform.core.model.processor.ProcessorRule;
import com.platform.utils.JexlUtils;

import us.codecraft.webmagic.Page;

/**
 * 解析规则接口类
 * 
 * @author 王龙
 * @date 2017年10月26日 下午7:04:11
 */
public interface RulePageProcessor {

    ProcessorRule getRule();

    /**
     * 是否匹配规则正确
     *
     * @param html
     *            页面
     *
     * @return 判断规则是否正确
     *
     * @description
     * @author 王龙
     * @date 2017/10/23 15:42
     */
    default boolean match(Page page) {
        String matchRule = getRule().getMatchRule();
        String matchRulePre = matchRule.substring(0, matchRule.indexOf(CrawlerContants.URL_PRE_SPLIT));
        switch (matchRulePre) {
        case CrawlerContants.MATCH_URL:
            return JexlUtils.jexlResult(page.getUrl(), CrawlerContants.MATCH_URL, matchRule);
        case CrawlerContants.MATCH_HTML:
            return JexlUtils.jexlResult(page.getHtml(), CrawlerContants.MATCH_HTML, matchRule);
        default:
            return JexlUtils.jexlResult(page.getHtml(), CrawlerContants.MATCH_HTML, matchRule);
        }
    }

    void processPage(Page page);
}
