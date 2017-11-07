package com.platform.core.contants;

/**
 * 解析类型
 * 
 * @author 王龙
 * @date 2017年10月26日 下午5:15:21
 */
public enum ProcessorType {
    PROCESSOR_LIST("列表页面", 1), PROCESSOR_LIST_PARAM("带参裂变页面", 2), PROCESSOR_DETAIL("详情页面", 3);

    // 构造方法
    private ProcessorType(String type, int index) {
    }
}
