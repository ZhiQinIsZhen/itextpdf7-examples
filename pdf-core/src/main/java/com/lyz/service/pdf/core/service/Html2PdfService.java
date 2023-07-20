package com.lyz.service.pdf.core.service;

import com.lyz.service.pdf.core.context.MyContext;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 16:06
 */
public interface Html2PdfService {

    /**
     * html模板转化为pdf
     *
     * @param fileName 文件名
     * @param template html模板
     * @param context 上下文
     * @return 文件地址
     */
    String process(String fileName, String template, MyContext context);
}
