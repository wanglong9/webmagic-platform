package com.platform.core.model.processor.list;

import com.platform.core.contants.ProcessorType;
import com.platform.core.model.AbstractBaseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * 普通列表页面规则，直接获取列表中的所有url
 *
 * @author 王龙
 * @date 2017-10-23 14：32
 */
@Getter
@Setter
public class CommonListProcessorRule extends AbstractBaseModel implements ListProcessorRule {

    private static final long serialVersionUID = 1L;
    /**
     * 匹配规则
     */
    private String matchRule;

    /** 下一页规则 */
    private String nextPageRule;

    /**
     * 获取解析url列表的规则,包含link().all()方法
     */
    private String urlsRule;

    /** 解析规则类型 */
    private ProcessorType type = ProcessorType.PROCESSOR_LIST;

}
