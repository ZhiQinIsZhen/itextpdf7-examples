package com.lyz.service.pdf.core.context;

import com.lyz.service.pdf.core.event.PageHeaderEvent;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.AbstractContext;

import java.util.Locale;
import java.util.Map;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 18:03
 */
public class MyContext extends AbstractContext {

    public MyContext() {
    }

    public MyContext(Locale locale) {
        super(locale);
    }

    public MyContext(Locale locale, Map<String, Object> variables) {
        super(locale, variables);
    }

    /**
     * 封面图片
     */
    @Getter
    @Setter
    private String coverImage;

    /**
     * 页眉
     */
    @Getter
    @Setter
    private PageHeaderEvent phEvent;

    /**
     * 页码
     */
    @Getter
    @Setter
    private PageSizeEvent psEvent;

    /**
     * 水印
     */
    @Getter
    @Setter
    private WaterMarkEvent wmEvent;
}
