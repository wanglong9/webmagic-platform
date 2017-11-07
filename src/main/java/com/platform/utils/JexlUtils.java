package com.platform.utils;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * 字符串表达式执行类
 *
 * @author 王龙
 * @date 2017-10-20 14：37
 */
public class JexlUtils {
    @SuppressWarnings("unchecked")
    public static <T> T jexlResult(Object paramObj, String paramName, String expression) {
        JexlContext context = new MapContext();
        JexlEngine engine = new JexlEngine();
        context.set(paramName, paramObj);
        Expression exp = engine.createExpression(expression);
        return (T) exp.evaluate(context);
    }

}
