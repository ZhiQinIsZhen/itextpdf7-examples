package com.lyz.service.pdf.controller.simple;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import com.lyz.service.pdf.core.MyResourceRetriever;
import com.lyz.service.pdf.core.MyTagWorkerFactory;
import com.lyz.service.pdf.core.handler.PageSizeEventHandler;
import com.lyz.service.pdf.core.handler.WaterMarkEventHandler;
import com.lyz.service.pdf.result.Result;
import com.lyz.service.pdf.util.PdfFontUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "Html demo")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/simple/html")
public class HtmlController {

    @Resource
    private ApplicationContext applicationContext;

    @ApiOperation("生成PDF文件-html转化PDF")
    @PostMapping("/create")
    public Result<String> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件名不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) throws Exception {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        engine.setTemplateResolver(resolver);
        Context context = new Context();
        context.setVariable("name", "424241312");
        String html = engine.process("html/aa", context);



        File file = FileUtil.createTempFile(filename, ".pdf");
        String absolutePath = file.getAbsolutePath();
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.setFullCompressionMode(true);
        PdfDocument pdf = new PdfDocument(new PdfWriter(absolutePath, writerProperties));
        pdf.setDefaultPageSize(PageSize.A3);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler());
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler("杭州至秦科技有限公司"));
        ConverterProperties properties = new ConverterProperties();
        properties.setImmediateFlush(false);
        properties.setBaseUri("templates/");
        properties.setResourceRetriever(new MyResourceRetriever("templates/"));
        properties.setFontProvider(new FontProvider(PdfFontUtil.getFontSet(), PdfFontUtil.FontEnum.PingFang_Regular.getFont()));
        properties.setTagWorkerFactory(new MyTagWorkerFactory(pdf));
        Document document = HtmlConverter.convertToDocument(html, pdf, properties);
        document.close();
        pdf.close();
        return Result.success(absolutePath);
    }
}
