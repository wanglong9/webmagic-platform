package com.platform.core.model.processor.list;

import com.platform.core.model.processor.ProcessorRule;

/**
 * 列表页面解析规则
 *
 * @author 王龙
 * @date 2017-10-23 14：00
 */
public interface ListProcessorRule extends ProcessorRule {

    /**
     * 获取获取【下一页】的规则
     * 
     * @description
     * @author 王龙
     * @date 2017年10月26日 下午4:48:52
     * @return
     */
    String getNextPageRule();

    /**
     * 获取列表的上一级node节点规则
     * 
     * @description
     * @author 王龙
     * @date 2017年10月26日 下午5:01:54
     * @return
     */
    String getUrlsRule();
}
