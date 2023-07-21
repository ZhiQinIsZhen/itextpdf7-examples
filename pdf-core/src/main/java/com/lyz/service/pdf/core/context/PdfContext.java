package com.lyz.service.pdf.core.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/20 17:27
 */
public class PdfContext {

    private static final String FOOTER_DIV_HEIGHT = "footerDivHeight";

    private static final ThreadLocal<Map<String, Object>> context = new InheritableThreadLocal<>();

    public static void remove() {
        context.remove();
    }

    public static float getFooterDivHeight() {
        Map<String, Object> map = context.get();
        if (Objects.isNull(map) || !map.containsKey(FOOTER_DIV_HEIGHT)) {
            return 0f;
        }
        return (float) map.getOrDefault(FOOTER_DIV_HEIGHT, 0f);
    }

    public static void putFooterDivHeight(float footerDivHeight) {
        init();
        context.get().put(FOOTER_DIV_HEIGHT, footerDivHeight);
    }

    private static void init() {
        if (Objects.isNull(context.get())) {
            context.set(new HashMap<>(8));
        }
    }
}
