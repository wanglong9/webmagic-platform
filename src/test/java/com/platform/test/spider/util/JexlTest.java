package com.platform.test.spider.util;
import org.junit.Test;
import com.platform.utils.JexlUtils;

import java.util.HashMap;
import java.util.Map;

public class JexlTest {
    @Test
    public void testJexl() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "b");
        String expression = "map.get(\"a\").equals(\"b\")";
        System.out.println(JexlUtils.jexlResult (map,"map",expression).toString());
    }
}
