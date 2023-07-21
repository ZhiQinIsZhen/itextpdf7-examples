package com.lyz.service.pdf.core.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/20 16:09
 */
@Getter
@Setter
public class PageFooterEvent implements Serializable {
    private static final long serialVersionUID = -3217631625101968680L;

    /**
     * 页眉起始页
     */
    private int startPageNumber = 1;

    /**
     * 高度
     */
    private float height = 23.5f;

    /**
     * 透明度
     */
    private float opacity = 0.1f;

    /**
     * 内容文本
     */
    private String context;

    /**
     * 字体大小
     */
    private float fontSize = 13f;
}
