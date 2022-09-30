package com.lyz.service.pdf.controller;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfChoiceFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLineAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
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
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "demo4")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/demo4")
public class Demo4Controller {

    @ApiOperation("生成PDF文件--添加注解--划线")
    @PostMapping("/create1")
    public Result<Boolean> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件生成地址不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler());
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler("杭州至秦科技有限公司"));
        PdfPage page = pdf.addNewPage();
        PdfArray lineEndings = new PdfArray();
        lineEndings.add(new PdfName("Diamond"));
        lineEndings.add(new PdfName("Diamond"));
        PdfAnnotation annotation = new PdfLineAnnotation(
                new Rectangle(0, 0),
                new float[]{20, 790, page.getPageSize().getWidth() - 20, 790})
                .setLineEndingStyles((lineEndings))
                .setContentsAsCaption(true)
                .setTitle(new PdfString("iText"))
                .setContents("The example of line annotation")
                .setColor(new DeviceRgb(0,0,255));
        page.addAnnotation(annotation);
        pdf.close();
        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("生成PDF文件--添加注解--文本")
    @PostMapping("/create2")
    public Result<Boolean> create2(@ApiParam(name = "filename")
                                  @Valid @NotBlank(message = "文件生成地址不能为空")
                                  @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler());
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler("杭州至秦科技有限公司"));
        Document document = new Document(pdf);
        document.add(new Paragraph("一个文本注解例子").setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.WRYH)));

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

    @ApiOperation("生成PDF文件--添加注解--链接")
    @PostMapping("/create3")
    public Result<Boolean> create3(@ApiParam(name = "filename")
                                   @Valid @NotBlank(message = "文件生成地址不能为空")
                                   @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler());
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler("杭州至秦科技有限公司"));
        Document document = new Document(pdf);


        PdfLinkAnnotation annotation = new PdfLinkAnnotation(new Rectangle(0, 0))
                .setAction(PdfAction.createURI("https://www.baidu.com"));

        Link link = new Link("链接", annotation);

        document.add(new Paragraph("一个链接注解例子").setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.WRYH))
        .add(link));

        document.close();
        return Result.success(Boolean.TRUE);
    }

    @ApiOperation("生成PDF文件--添加注解--交互式")
    @PostMapping("/create4")
    public Result<Boolean> create4(@ApiParam(name = "filename")
                                   @Valid @NotBlank(message = "文件生成地址不能为空")
                                   @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler());
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler("杭州至秦科技有限公司"));
        Document document = new Document(pdf);
        Paragraph title = new Paragraph("Application for employment")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16);
        document.add(title);
        document.add(new Paragraph("Full name:").setFontSize(12));
        document.add(new Paragraph("Native language:      English         French       German        Russian        Spanish").setFontSize(12));
        document.add(new Paragraph("Experience in:       cooking        driving           software development").setFontSize(12));
        document.add(new Paragraph("Preferred working shift:").setFontSize(12));
        document.add(new Paragraph("Additional information:").setFontSize(12));

        //Add acroform
        PdfAcroForm form = PdfAcroForm.getAcroForm(document.getPdfDocument(), true);
        //Create text field
        PdfTextFormField nameField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(99, 753, 425, 15), "name", "");
        form.addField(nameField);
        //Create radio buttons
        PdfButtonFormField group = PdfFormField.createRadioGroup(document.getPdfDocument(), "language", "");
        PdfFormField.createRadioButton(document.getPdfDocument(), new Rectangle(130, 728, 15, 15), group, "English");
        PdfFormField.createRadioButton(document.getPdfDocument(), new Rectangle(200, 728, 15, 15), group, "French");
        PdfFormField.createRadioButton(document.getPdfDocument(), new Rectangle(260, 728, 15, 15), group, "German");
        PdfFormField.createRadioButton(document.getPdfDocument(), new Rectangle(330, 728, 15, 15), group, "Russian");
        PdfFormField.createRadioButton(document.getPdfDocument(), new Rectangle(400, 728, 15, 15), group, "Spanish");
        form.addField(group);
        //Create checkboxes
        for (int i = 0; i < 3; i++) {
            PdfButtonFormField checkField = PdfFormField.createCheckBox(document.getPdfDocument(), new Rectangle(119 + i * 69, 701, 15, 15),
                    "experience".concat(String.valueOf(i+1)), "Off", PdfFormField.TYPE_CHECK);
            form.addField(checkField);
        }

        //Create combobox
        String[] options = {"Any", "6.30 am - 2.30 pm", "1.30 pm - 9.30 pm"};
        PdfChoiceFormField choiceField = PdfFormField.createComboBox(document.getPdfDocument(), new Rectangle(163, 676, 115, 15),
                "shift", "Any", options);
        form.addField(choiceField);

        //Create multiline text field
        PdfTextFormField infoField = PdfTextFormField.createMultilineText(document.getPdfDocument(),
                new Rectangle(158, 625, 366, 40), "info", "");
        form.addField(infoField);

        //Create push button field
        PdfButtonFormField button = PdfFormField.createPushButton(document.getPdfDocument(),
                new Rectangle(479, 594, 45, 15), "reset", "RESET");
        button.setAction(PdfAction.createResetForm(new String[] {"name", "language", "experience1", "experience2", "experience3", "shift", "info"}, 0));
        form.addField(button);

        document.close();
        return Result.success(Boolean.TRUE);
    }
}
