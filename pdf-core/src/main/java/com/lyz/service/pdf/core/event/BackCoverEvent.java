package com.lyz.service.pdf.core.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/22 10:35
 */
@Getter
@Setter
public class BackCoverEvent implements Serializable {
    private static final long serialVersionUID = 2662678734270919622L;

    /**
     * 封底图片
     */
    private String backCoverImage;
}
