package com.lyz.service.pdf.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import com.lyz.service.pdf.handler.PageSizeEventHandler;
import com.lyz.service.pdf.handler.WaterMarkEventHandler;
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
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "demo6")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/demo6")
public class Demo6Controller {

    @ApiOperation("生成PDF文件-html转化PDF")
    @PostMapping("/create")
    public Result<Boolean> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件生成地址不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) throws Exception {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        Document document = new Document(pdf);
//        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler());
//        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler("杭州至秦科技有限公司"));
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        FontProgram fontProgram = FontProgramFactory.createFont(PdfFontUtil.FontEnum.WRYH.getPath());
        fontProvider.addFont(fontProgram);
        converterProperties.setFontProvider(fontProvider);
        InputStream htmlStream = new FileInputStream("C:\\code\\liyz\\itextpdf7-examples\\src\\main\\resources\\templates\\html\\aa.html");
        HtmlConverter.convertToDocument(htmlStream, pdf, converterProperties);
        document.close();
        return Result.success(Boolean.TRUE);
    }
}
