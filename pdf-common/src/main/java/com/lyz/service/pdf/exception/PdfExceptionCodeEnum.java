package com.lyz.service.pdf.exception;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/18 16:51
 */
public enum PdfExceptionCodeEnum implements IExceptionService{
    SUCCESS("0", "成功"),
    FAIL("1", "失败"),
    PARAMS_VALIDATED("10000", "参数校验失败"),
    SERVICE_FAIL("10005", "服务异常"),
    DEEP_COPY_ERROR("11001", "对象深拷贝错误"),
    CREATE_PDF_FAIL("10002", "生成PDF文件失败"),
    CREATE_PDF_FONT_FAIL("10003", "生成PDF字体失败"),
    ;

    private final String code;

    private final String message;

    PdfExceptionCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
