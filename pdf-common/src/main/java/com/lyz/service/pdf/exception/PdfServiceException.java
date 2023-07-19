package com.lyz.service.pdf.exception;

import lombok.Getter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/18 16:49
 */
public class PdfServiceException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public PdfServiceException() {
        this(PdfExceptionCodeEnum.FAIL);
    }

    public PdfServiceException(IExceptionService codeService) {
        super(codeService.getMessage());
        this.code = codeService.getCode();
    }

    /**
     * 异常code
     */
    @Getter
    private final String code;
}
