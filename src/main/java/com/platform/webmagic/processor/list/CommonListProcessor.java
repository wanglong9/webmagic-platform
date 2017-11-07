package com.platform.webmagic.processor.list;

import java.util.List;

import com.platform.core.contants.CrawlerContants;
import com.platform.core.model.processor.list.CommonListProcessorRule;
import com.platform.core.model.processor.list.ListProcessorRule;
import com.platform.utils.JexlUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

/**
 * 普通列表页面规则
 *
 * @author 王龙
 * @date 2017-10-23 14：32
 */
public class CommonListProcessor extends ListProcessor {

    private CommonListProcessorRule rule;

    public CommonListProcessor(CommonListProcessorRule rule) {
        this.rule=rule;
    }

    /**
     * 需要追加的url和额外添加的页面属性
     *
     * @description
     * @author 王龙
     * @date 2017/10/23 15:12
     */
    public List<String> getAddTargetRequest(Html html) {
        List<String> urlList = JexlUtils.jexlResult(html, CrawlerContants.MATCH_HTML, rule.getUrlsRule());
        return urlList;
    }

    @Override
    public void processPage(Page page) {
        super.processPage(page);
        // 普通列表页面
        List<String> addTargetRequest = this.getAddTargetRequest(page.getHtml());
        page.addTargetRequests(addTargetRequest);
    }

    @Override
    public ListProcessorRule getRule() {
        return rule;
    }

}
