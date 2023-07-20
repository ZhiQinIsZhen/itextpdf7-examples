package com.lyz.service.pdf.controller.simple;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
import com.lyz.service.pdf.core.handler.CoverEventHandler;
import com.lyz.service.pdf.core.handler.PageSizeEventHandler;
import com.lyz.service.pdf.core.handler.WaterMarkEventHandler;
import com.lyz.service.pdf.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 15:24
 */
@Api(tags = "背景demo")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/simple/cover")
public class CoverController {

    @ApiOperation("生成PDF文件--设置PDF背景")
    @PostMapping("/create")
    public Result<String> create(@ApiParam(name = "filename")
                                  @Valid @NotBlank(message = "文件名不能为空")
                                  @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        File file = FileUtil.createTempFile(filename, ".pdf");
        String absolutePath = file.getAbsolutePath();
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.setFullCompressionMode(true);
        PdfDocument pdf = new PdfDocument(new PdfWriter(absolutePath, writerProperties));
        pdf.setDefaultPageSize(PageSize.A3);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler(new PageSizeEvent()));
        WaterMarkEvent waterMarkEvent = new WaterMarkEvent();
        waterMarkEvent.setWaterMark("杭州至秦科技有限公司");
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler(waterMarkEvent));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new CoverEventHandler());
        Document document = new Document(pdf, pdf.getDefaultPageSize(), false);
        document.close();
        return Result.success(absolutePath);
    }
}
