package com.lyz.service.pdf.exception;

import lombok.Getter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:23
 */
public class PdfException extends RuntimeException{
    private static final long serialVersionUID = -2267128739035800967L;

    /**
     * 获取异常code
     */
    @Getter
    private String code;

    public PdfException(String code, String message) {
        super(message);
        this.code = code;
    }

    public PdfException(IExceptionCodeService codeService) {
        super(codeService.getMessage());
        this.code = codeService.getCode();
    }
}
