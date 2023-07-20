package com.lyz.service.pdf.controller.simple;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.lyz.service.pdf.constant.PdfServiceConstant;
import com.lyz.service.pdf.core.event.PageHeaderEvent;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
import com.lyz.service.pdf.core.handler.PageHeaderEventHandler;
import com.lyz.service.pdf.core.handler.PageSizeEventHandler;
import com.lyz.service.pdf.core.handler.WaterMarkEventHandler;
import com.lyz.service.pdf.result.Result;
import com.lyz.service.pdf.util.DateUtil;
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
 * @date 2023/7/20 11:02
 */
@Api(tags = "页眉页尾demo")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/simple/page")
public class PageHeaderController {

    @ApiOperation("生成PDF文件--设置页眉")
    @PostMapping("/header/create")
    public Result<String> headerCreate(@ApiParam(name = "filename")
                                 @Valid @NotBlank(message = "文件名不能为空")
                                 @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        File file = FileUtil.createTempFile(filename, ".pdf");
        String absolutePath = file.getAbsolutePath();
        WriterProperties writerProperties = new WriterProperties();
        writerProperties.setFullCompressionMode(true);
        PdfDocument pdf = new PdfDocument(new PdfWriter(absolutePath, writerProperties));
        pdf.setDefaultPageSize(PageSize.A3);
        WaterMarkEvent waterMarkEvent = new WaterMarkEvent();
        waterMarkEvent.setWaterMark("杭州至秦科技有限公司");
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler(waterMarkEvent));
        PageSizeEvent pageSizeEvent = new PageSizeEvent();
        pageSizeEvent.setPrefix("第");
        pageSizeEvent.setSuffix("页");
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler(pageSizeEvent));

        PageHeaderEvent event = new PageHeaderEvent();
        event.setStartPageNumber(1);
        event.setContext("这是一个页眉测试Demo");
        event.setBkImage(PdfServiceConstant.ROOT_TEMPLATE_IMAGE + "ph/ph1.png");
        event.setTime(DateUtil.currentDate());
        event.setFlag("这个一个Flag");
        event.setLogoImage(PdfServiceConstant.ROOT_TEMPLATE_IMAGE + "logo/logo1.png");
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageHeaderEventHandler(event));
        Document document = new Document(pdf, pdf.getDefaultPageSize(), false);
        document.close();
        return Result.success(absolutePath);
    }
}
