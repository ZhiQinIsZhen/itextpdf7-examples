package com.lyz.service.pdf.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lyz.service.pdf.exception.PdfException;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import com.lyz.service.pdf.result.Result;
import com.lyz.service.pdf.util.PdfFontUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "demo1")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/demo1")
public class Demo1Controller {

    @ApiOperation("生成PDF文件--一段以Hello Word")
    @PostMapping("/create")
    public Result<Boolean> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件生成地址不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        try {
            document.add(new Paragraph("Hello World!").setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.WRYH)));
        } catch (Throwable e) {
            log.error("create pdf file fail. ", e);
            document.close();
            try {
                Files.delete(Paths.get(filename));
            } catch (IOException ioException) {
                log.warn("delete pdf file path fail, fileName : {}", filename);
            }
            throw new PdfException(PdfExceptionCodeEnum.CREATE_PDF_FAIL);
        }
        document.close();
        return Result.success(Boolean.TRUE);
    }
}
