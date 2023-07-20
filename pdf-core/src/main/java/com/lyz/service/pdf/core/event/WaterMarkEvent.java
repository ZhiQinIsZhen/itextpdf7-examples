package com.lyz.service.pdf.core.event;

import lombok.*;

import java.io.Serializable;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/20 13:47
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaterMarkEvent implements Serializable {
    private static final long serialVersionUID = -5492519034322034397L;

    /**
     * 起始页
     */
    private int startPageNumber = 1;

    /**
     * 文本
     */
    private String waterMark;

    /**
     * 字体大小
     */
    private float fontSize = 18f;

    /**
     * 透明度
     */
    private float opacity = 1f;

    /**
     * 旋转角度
     */
    private float radAngle = 45f;
}
