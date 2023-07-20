package com.lyz.service.pdf.core.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/20 9:36
 */
@Getter
@Setter
public class PageHeaderEvent implements Serializable {
    private static final long serialVersionUID = 3173457759211493035L;

    /**
     * 页眉起始页
     */
    private int startPageNumber = 1;

    /**
     * 背景图片
     */
    private String bkImage;

    /**
     * 高度
     */
    private float height = 63f;

    /**
     * 文本内容
     */
    private String context;

    /**
     * 时间
     */
    private Date time;

    /**
     * logo
     */
    private String logoImage;

    /**
     * flag
     */
    private String flag;
}
