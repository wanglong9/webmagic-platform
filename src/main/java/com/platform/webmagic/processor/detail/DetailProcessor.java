package com.platform.webmagic.processor.detail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.platform.core.contants.CrawlerContants;
import com.platform.core.model.processor.ProcessorRule;
import com.platform.core.model.processor.detail.DetailProcessorRule;
import com.platform.utils.JexlUtils;
import com.platform.webmagic.processor.RulePageProcessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

/**
 * 详情页面解析规则
 *
 * @author 王龙
 * @date 2017-10-23
 * @time 14:00
 */
public class DetailProcessor implements RulePageProcessor {

    private DetailProcessorRule rule;

    public DetailProcessor(DetailProcessorRule rule) {
        this.rule = rule;
    }

    @Override
    public void processPage(Page page) {
        addRequestExtraParams2Field(page);
        addAnalysisParams2Field(page);
    }

    /** 
     * 将页面中需要解析的属性，添加到field中
     * @description
     * @author 王龙
     * @date 2017年10月27日 上午10:20:51 
     * @param page 
     */
    private void addAnalysisParams2Field(Page page) {
        Map<String, Object> fieldMap = this.getAddFields(page.getHtml());
        for (String key : fieldMap.keySet()) {
            page.putField(key, fieldMap.get(key));
        }
    }

    /**
     * 将request中附带的extras属性，添加到field中。
     *
     * @description
     * @author 王龙
     * @date 2017/10/23 15:26
     */
    private void addRequestExtraParams2Field(Page page) {
        Map<String, Object> extras = page.getRequest().getExtras();
        if (null != extras) {
            for (String key : extras.keySet()) {
                if (key.startsWith(CrawlerContants.EXTRAS_PARAMS_PRE)) {
                    page.putField(key.substring(CrawlerContants.EXTRAS_PARAMS_PRE.length() + 1), extras.get(key));
                }
            }
        }
    }

    /**
     * 获取详情页面需要解析的字段
     *
     * @description
     * @author 王龙
     * @date 2017/10/23 15:33
     */

    private Map<String, Object> getAddFields(Html html) {
        HashMap<String, Object> fieldsMap = new HashMap<>();
        Set<String> keySet = rule.getAnalysisRule().keySet();
        for (String key : keySet) {
            String value = JexlUtils.jexlResult(html, CrawlerContants.MATCH_HTML, rule.getAnalysisRule().get(key));
            fieldsMap.put(key, value);
        }
        return fieldsMap;
    }

    @Override
    public ProcessorRule getRule() {
        return rule;
    }
}
