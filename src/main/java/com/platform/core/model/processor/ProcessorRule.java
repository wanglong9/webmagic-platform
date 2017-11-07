package com.platform.core.model.processor;

import com.platform.core.contants.ProcessorType;

/**
 * 解析类规则接口
 * 
 * @author 王龙
 * @date 2017年10月26日 下午4:47:46
 */
public interface ProcessorRule {

    /**
     * 获取页面匹配规则
     * 
     * @description
     * @author 王龙
     * @date 2017年10月26日 下午4:47:58
     * @return
     */
    String getMatchRule();

    /**
     * 获取解析类型名称
     * 
     * @description
     * @author 王龙
     * @date 2017年10月26日 下午5:00:10
     * @return
     */
    ProcessorType getType();
}
