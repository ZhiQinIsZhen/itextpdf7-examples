package com.lyz.service.pdf.controller.simple;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfChoiceFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
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
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "交互demo")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/simple/field")
public class FieldController {

    @ApiOperation("生成PDF文件--添加注解--交互式")
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
