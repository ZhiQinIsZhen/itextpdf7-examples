package com.lyz.service.pdf.controller.simple;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
import com.lyz.service.pdf.core.handler.PageSizeEventHandler;
import com.lyz.service.pdf.core.handler.WaterMarkEventHandler;
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
import java.util.Map;
import java.util.TreeMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "目录demo")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/simple/dire")
public class DireController {

    public static final Map<String, Integer> TheRevenantNominations = new TreeMap<String, Integer>();
    static {
        TheRevenantNominations.put("Performance by an actor in a leading role", 4);
        TheRevenantNominations.put("Performance by an actor in a supporting role", 4);
        TheRevenantNominations.put("Achievement in cinematography", 4);
        TheRevenantNominations.put("Achievement in costume design", 5);
        TheRevenantNominations.put("Achievement in directing", 5);
        TheRevenantNominations.put("Achievement in film editing", 6);
        TheRevenantNominations.put("Achievement in makeup and hairstyling", 7);
        TheRevenantNominations.put("Best motion picture of the year", 8);
        TheRevenantNominations.put("Achievement in production design", 8);
        TheRevenantNominations.put("Achievement in sound editing", 9);
        TheRevenantNominations.put("Achievement in sound mixing", 9);
        TheRevenantNominations.put("Achievement in visual effects", 10);
    }

    @ApiOperation("生成PDF文件--设置PDF目录")
    @PostMapping("/create")
    public Result<Boolean> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件生成地址不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler(new PageSizeEvent()));
        WaterMarkEvent waterMarkEvent = new WaterMarkEvent();
        waterMarkEvent.setWaterMark("杭州至秦科技有限公司");
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler(waterMarkEvent));
        Document document = new Document(pdf);
        document.add(new Paragraph("目录").setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Bold)).setFontSize(25).setTextAlignment(TextAlignment.CENTER));
        for (Map.Entry<String, Integer> entry : TheRevenantNominations.entrySet()) {
            PdfPage pdfPage = pdf.addNewPage();
            //Add destination
            String destinationKey = "p" + (pdf.getNumberOfPages() - 1);
            PdfArray destinationArray = new PdfArray();
            destinationArray.add(pdfPage.getPdfObject());
            destinationArray.add(PdfName.XYZ);
            destinationArray.add(new PdfNumber(0));
            destinationArray.add(new PdfNumber(pdfPage.getMediaBox().getHeight()));
            destinationArray.add(new PdfNumber(1));
            pdf.addNamedDestination(destinationKey, destinationArray);

            //Add TOC line with bookmark
            Paragraph p = new Paragraph();
            p.setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.WeiRuanYaHei_Regular));
            p.addTabStops(new TabStop(540, TabAlignment.RIGHT, new DottedLine()));
            p.add(entry.getKey());
            p.add(new Tab());
            p.add(String.valueOf(pdf.getNumberOfPages()));
            p.setProperty(Property.ACTION, PdfAction.createGoTo(destinationKey));
            document.add(p);
        }
        // close the document
        document.close();
        return Result.success(Boolean.TRUE);
    }
}
