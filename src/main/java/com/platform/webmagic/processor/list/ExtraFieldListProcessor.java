package com.platform.webmagic.processor.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.platform.core.contants.CrawlerContants;
import com.platform.core.model.processor.list.ExtraFieldListProcessorRule;
import org.apache.commons.lang3.StringUtils;

import com.platform.core.model.processor.list.ListProcessorRule;
import com.platform.utils.JexlUtils;

import lombok.Getter;
import lombok.Setter;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * 带参数的列表页面规则
 *
 * @author 王龙
 * @date 2017-10-23 16：00
 */
@Getter
@Setter
public class ExtraFieldListProcessor extends ListProcessor {
    private ExtraFieldListProcessorRule rule;

    public ExtraFieldListProcessor(ExtraFieldListProcessorRule rule) {
        this.rule = rule;
    }

    @Override
    public void processPage(Page page) {
        super.processPage(page);
        List<Request> addTargetRequest = this.getAddTargetRequest(page.getHtml(), CrawlerContants.EXTRAS_PARAMS_PRE,
                page.getUrl().toString());
        for (Request request : addTargetRequest) {
            page.addTargetRequest(request);
        }
    }

    /**
     * 添加request
     *
     * @description
     * @author 王龙
     * @date 2017/10/23 16:48
     */
    public List<Request> getAddTargetRequest(Html html, String extrasParamsPre, String url) {
        List<Request> targetRequestList = new ArrayList<>();
        List<Selectable> urlList = JexlUtils.jexlResult(html, CrawlerContants.MATCH_HTML, rule.getUrlsRule());
        for (Selectable node : urlList) {
            String targetUrl = JexlUtils.jexlResult(node, CrawlerContants.MATCH_HTML, rule.getUrlParamRule());
            Request request = getRequest(targetUrl, url);
            if (null != request) {
                Map<String, Object> fieldsMap = getStringFieldsMap(extrasParamsPre, node);
                request.setExtras(fieldsMap);
                targetRequestList.add(request);
            }
        }
        return targetRequestList;
    }

    /**
     * 获取节点的附带参数
     *
     * @description
     * @author 王龙
     * @date 2017/10/23 16:54
     */
    private Map<String, Object> getStringFieldsMap(String extrasParamsPre, Selectable node) {
        Map<String, Object> fieldsMap = new HashMap<>();
        if (null != rule.getParamRule()) {
            Set<String> keySet = rule.getParamRule().keySet();
            for (String key : keySet) {
                String value = JexlUtils.jexlResult(node, CrawlerContants.MATCH_HTML,
                        rule.getParamRule().get(key).toString());
                fieldsMap.put(extrasParamsPre + key, value);
            }
        }
        return fieldsMap;
    }

    /**
     * 将requestUrl转换成Requset对象
     *
     * @description
     * @author 王龙
     * @date 2017/10/23 16:14
     */
    private Request getRequest(String requestString, String url) {
        if (StringUtils.isBlank(requestString) || requestString.equals("#")) {
            return null;
        }
        requestString = UrlUtils.canonicalizeUrl(requestString, url.toString());
        return new Request(requestString);
    }

    @Override
    public ListProcessorRule getRule() {
        return rule;
    }
}
