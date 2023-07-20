package com.lyz.service.pdf.core.event;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/20 13:33
 */
@Getter
@Setter
public class PageSizeEvent implements Serializable {
    private static final long serialVersionUID = 7586975966998958929L;

    /**
     * 页眉起始页
     */
    private int startPageNumber = 1;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 后缀
     */
    private String suffix;

    /**
     * 字体大小
     */
    private float fontSize = 10f;

    /**
     * 透明度
     */
    private float opacity = 1f;

    /**
     * 获取pageSize文本
     *
     * @param pageNumber 页码
     * @return pageSize文本
     */
    public String getPageSizeText(int pageNumber) {
        return (StringUtils.isBlank(prefix) ? StringUtils.EMPTY : prefix.trim() + " ")
                .concat(String.valueOf(pageNumber))
                .concat(StringUtils.isBlank(suffix) ? StringUtils.EMPTY : " " + suffix.trim());
    }
}
