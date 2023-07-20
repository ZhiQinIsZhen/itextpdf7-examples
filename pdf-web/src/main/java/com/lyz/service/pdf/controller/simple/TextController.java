package com.lyz.service.pdf.controller.simple;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
import com.lyz.service.pdf.core.handler.PageSizeEventHandler;
import com.lyz.service.pdf.core.handler.WaterMarkEventHandler;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import com.lyz.service.pdf.exception.PdfServiceException;
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
@Api(tags = "文本demo")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/simple/text")
public class TextController {

    @ApiOperation("生成PDF文件--一段以Hello Word")
    @PostMapping("/create")
    public Result<Boolean> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件生成地址不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        try {
            document.add(new Paragraph("Hello World!").setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.WeiRuanYaHei_Regular)));
        } catch (Throwable e) {
            log.error("create pdf file fail. ", e);
            document.close();
            try {
                Files.delete(Paths.get(filename));
            } catch (IOException ioException) {
                log.warn("delete pdf file path fail, fileName : {}", filename);
            }
            throw new PdfServiceException(PdfExceptionCodeEnum.CREATE_PDF_FAIL);
        }
        document.close();
        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("生成PDF文件--基于事件机制增加页码")
    @PostMapping("/createPage")
    public Result<Boolean> createPage(@ApiParam(name = "filename")
                                  @Valid @NotBlank(message = "文件生成地址不能为空")
                                  @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler(new PageSizeEvent()));
        Document document = new Document(pdf);
        //add font
        PdfFont font = PdfFontUtil.createFont(PdfFontUtil.FontEnum.WeiRuanYaHei_Regular);
        document.add(new Paragraph("Hello World!").setFont(font).setFontSize(20f));
        pdf.addNewPage();
        pdf.addNewPage();
        pdf.addNewPage();
        document.close();
        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("生成PDF文件--增加水印功能")
    @PostMapping("/createWater")
    public Result<Boolean> createWater(@ApiParam(name = "filename")
                                  @Valid @NotBlank(message = "文件生成地址不能为空")
                                  @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        WaterMarkEvent waterMarkEvent = new WaterMarkEvent();
        waterMarkEvent.setWaterMark("杭州至秦科技有限公司");
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler(waterMarkEvent));
        Document document = new Document(pdf);
        //add font
        PdfFont font = PdfFontUtil.createFont(PdfFontUtil.FontEnum.WeiRuanYaHei_Regular);
        document.add(new Paragraph("Hello World!").setFont(font).setFontSize(20f));
        pdf.addNewPage();
        pdf.addNewPage();
        pdf.addNewPage();
        document.close();
        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("生成PDF文件--添加注解--文本")
    @PostMapping("/createAnnotation")
    public Result<Boolean> createAnnotation(@ApiParam(name = "filename")
                                   @Valid @NotBlank(message = "文件生成地址不能为空")
                                   @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler(new PageSizeEvent()));
        WaterMarkEvent waterMarkEvent = new WaterMarkEvent();
        waterMarkEvent.setWaterMark("杭州至秦科技有限公司");
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler(waterMarkEvent));
        Document document = new Document(pdf);
        document.add(new Paragraph("一个文本注解例子").setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.WeiRuanYaHei_Regular)));

        PdfAnnotation annotation = new PdfTextAnnotation(new Rectangle(20, 800, 0, 0))
                .setOpen(true)
                .setColor(new DeviceRgb(0, 255, 0))
                .setTitle(new PdfString("This is Tile!"))
                .setContents(new PdfString("You are a little Rubicon"))
                .setDate(new PdfString("2022-01-14"));

        pdf.getFirstPage().addAnnotation(annotation);

        document.close();
        return Result.success(Boolean.TRUE);
    }
}
