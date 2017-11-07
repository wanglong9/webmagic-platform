package com.platform.core.model.processor.detail;

import java.util.Map;

import com.platform.core.contants.ProcessorType;
import com.platform.core.model.AbstractBaseModel;
import com.platform.core.model.processor.ProcessorRule;

import lombok.Getter;
import lombok.Setter;

/**
 * 详情页面解析规则
 *
 * @author 王龙
 * @date 2017-10-23
 * @time 14:00
 */
@Getter
@Setter
public class DetailProcessorRule extends AbstractBaseModel implements ProcessorRule {
    private static final long serialVersionUID = 1L;
    /**
     * 匹配规则
     */
    private String matchRule;
    /**
     * 页面解析字段规则
     */
    private Map<String, String> analysisRule;

    /** 解析规则类型 */
    private ProcessorType type = ProcessorType.PROCESSOR_DETAIL;
}
