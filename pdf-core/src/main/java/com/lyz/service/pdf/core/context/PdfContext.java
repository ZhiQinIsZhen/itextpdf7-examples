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

    private static final String COVER_EXIST = "cover";
    private static final String FOOTER_DIV_HEIGHT = "footerDivHeight";
    private static final String TOTAL_PAGE = "totalPage";

    private static final ThreadLocal<Map<String, Object>> context = new InheritableThreadLocal<>();

    public static void remove() {
        context.remove();
    }

    /**
     * 获取是否有封面
     *
     * @return 是或否
     */
    public static boolean getCover() {
        Map<String, Object> map = context.get();
        if (Objects.isNull(map) || !map.containsKey(COVER_EXIST)) {
            return false;
        }
        return (boolean) map.getOrDefault(COVER_EXIST, false);
    }

    /**
     * 设置是否有封面
     *
     * @param cover 是或否
     */
    public static void putCover(boolean cover) {
        init();
        context.get().put(COVER_EXIST, cover);
    }

    /**
     * 获取页尾高度
     *
     * @return 页尾高度
     */
    public static float getFooterDivHeight() {
        Map<String, Object> map = context.get();
        if (Objects.isNull(map) || !map.containsKey(FOOTER_DIV_HEIGHT)) {
            return 0f;
        }
        return (float) map.getOrDefault(FOOTER_DIV_HEIGHT, 0f);
    }

    /**
     * 设置页尾高度
     *
     * @param footerDivHeight 页尾高度
     */
    public static void putFooterDivHeight(float footerDivHeight) {
        init();
        context.get().put(FOOTER_DIV_HEIGHT, footerDivHeight);
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public static int getTotalPage() {
        Map<String, Object> map = context.get();
        if (Objects.isNull(map) || !map.containsKey(TOTAL_PAGE)) {
            return Integer.MAX_VALUE;
        }
        return (int) map.getOrDefault(TOTAL_PAGE, Integer.MAX_VALUE);
    }

    /**
     * 设置总页数
     *
     * @param totalPage 总页数
     */
    public static void putTotalPage(int totalPage) {
        init();
        context.get().put(TOTAL_PAGE, totalPage);
    }

    /**
     * 初始化
     */
    private static void init() {
        if (Objects.isNull(context.get())) {
            context.set(new HashMap<>(8));
        }
    }
}
