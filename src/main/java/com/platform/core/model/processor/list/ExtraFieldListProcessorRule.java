package com.platform.core.model.processor.list;

import java.util.Map;

import com.platform.core.contants.ProcessorType;
import com.platform.core.model.AbstractBaseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * 带参数的列表页面规则，对每一条url进行带参操作，所有参数以paramRule形式传递
 *
 * @author 王龙
 * @date 2017-10-23 16：00
 */
@Getter
@Setter
public class ExtraFieldListProcessorRule extends AbstractBaseModel implements ListProcessorRule {
    private static final long serialVersionUID = 1L;
    /**
     * 匹配规则
     */
    private String matchRule;
    /**
     * 下一页规则
     */
    private String nextPageRule;

    /**
     * 获取解析url列表的规则,列表的上一级xpath规则,不包含link().all()方法
     */
    private String urlsRule;
    /**
     * 目标url的规则
     */
    private String urlParamRule;
    /**
     * 其他属性的规则，key为中文名称，value为xpath
     */
    private Map<String, Object> paramRule;

    /** 解析规则类型 */
    private ProcessorType type = ProcessorType.PROCESSOR_LIST_PARAM;

}
