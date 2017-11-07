package com.platform.webmagic.processor;

import java.util.ArrayList;
import java.util.List;

import com.platform.core.contants.ProcessorType;
import com.platform.core.model.processor.ProcessorRule;
import com.platform.core.model.processor.detail.DetailProcessorRule;
import com.platform.core.model.processor.list.ExtraFieldListProcessorRule;
import com.platform.webmagic.processor.detail.DetailProcessor;
import com.platform.webmagic.processor.list.CommonListProcessor;
import com.platform.webmagic.processor.list.ExtraFieldListProcessor;
import com.platform.core.model.processor.list.CommonListProcessorRule;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 聚合多RulePageProcessor的类，继续过程根据配置参数选择实际的processor
 * 
 * @author 王龙
 * @date 2017年10月26日 下午7:10:34
 */
public class CompositePageProcessor implements PageProcessor {

    private Site site;

    private List<RulePageProcessor> pageProcessorList;

    public CompositePageProcessor(List<ProcessorRule> ruleList) {
        this.site = Site.me();
        this.pageProcessorList = getPageProcessor(ruleList);
    }

    public CompositePageProcessor(Site site, List<ProcessorRule> ruleList) {
        this.site = site;
        this.pageProcessorList = getPageProcessor(ruleList);
    }

    @Override
    public void process(Page page) {
        if (null == pageProcessorList) {
            return;
        } else {
            for (RulePageProcessor rulePageProcessor : pageProcessorList) {
                if (rulePageProcessor.match(page)) {
                    rulePageProcessor.processPage(page);
                    return;
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    /** 
     * 将解析配置规则processorRule转换成解析器processor
     * @description
     * @author 王龙
     * @date 2017年10月31日 下午2:54:08 
     * @param ruleList
     * @return 
     */
    private List<RulePageProcessor> getPageProcessor(List<ProcessorRule> ruleList) {
        pageProcessorList = new ArrayList<>();

        ruleList.forEach(rule -> {
            RulePageProcessor pageProcessor = null;
            switch (rule.getType()) {
            case ProcessorType.PROCESSOR_DETAIL:
                pageProcessor = new DetailProcessor((DetailProcessorRule) rule);
                break;
            case ProcessorType.PROCESSOR_LIST:
                pageProcessor = new CommonListProcessor((CommonListProcessorRule) rule);
                break;
            case ProcessorType.PROCESSOR_LIST_PARAM:
                pageProcessor = new ExtraFieldListProcessor((ExtraFieldListProcessorRule) rule);
                break;
            default:
                break;
            }
            pageProcessorList.add(pageProcessor);
        });
        return pageProcessorList;
    }
}
