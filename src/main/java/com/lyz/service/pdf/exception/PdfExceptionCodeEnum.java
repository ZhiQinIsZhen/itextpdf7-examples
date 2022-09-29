package com.lyz.service.pdf.exception;

import lombok.AllArgsConstructor;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:25
 */
@AllArgsConstructor
public enum PdfExceptionCodeEnum implements IExceptionCodeService{
    SUCCESS("0", "成功"),
    FAIL("1", "失败"),

    PARAMS_VALIDATED("10000", "参数校验失败"),
    UNKNOWN_EXCEPTION("10001", "未知异常"),
    CREATE_PDF_FAIL("10002", "生成PDF文件失败"),
    CREATE_PDF_FONT_FAIL("10003", "生成PDF字体失败"),
    ;

    private String code;

    private String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
