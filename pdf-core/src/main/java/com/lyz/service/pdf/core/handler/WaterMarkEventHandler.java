package com.lyz.service.pdf.core.handler;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
import com.lyz.service.pdf.util.PdfFontUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 注释:水印事件
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 16:50
 */
public class WaterMarkEventHandler implements IEventHandler {

    private final WaterMarkEvent waterMarkEvent;

    public WaterMarkEventHandler(WaterMarkEvent waterMarkEvent) {
        this.waterMarkEvent = waterMarkEvent;
    }

    @Override
    public void handleEvent(Event event) {
        String waterMark;
        if (Objects.isNull(waterMarkEvent) || StringUtils.isBlank(waterMark = waterMarkEvent.getWaterMark())) {
            return;
        }
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        int pageNumber = pdfDocument.getNumberOfPages();
        if (pageNumber < waterMarkEvent.getStartPageNumber()) {
            return;
        }
        PdfPage page = documentEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), documentEvent.getDocument());
        JLabel label = new JLabel();
        label.setText(waterMark);
        FontMetrics metrics = label.getFontMetrics(label.getFont());
        int textWidth = metrics.stringWidth(label.getText());
        //add font
        PdfFont font = PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Regular);
        Paragraph paragraph = new Paragraph(waterMark)
                .setFont(font)
                .setFontSize(waterMarkEvent.getFontSize())
                .setFontColor(new DeviceRgb(237,237,237));
        //水印文本透明度
        PdfExtGState gs = new PdfExtGState().setFillOpacity(waterMarkEvent.getOpacity());
        pdfCanvas.saveState();
        pdfCanvas.setExtGState(gs);
        Canvas canvas = new Canvas(pdfCanvas, documentEvent.getDocument().getDefaultPageSize());
        //循环添加水印
        for(float posX = 75f; posX < pageSize.getWidth(); posX = posX + textWidth * 1.2f){
            for(float posY = 50f; posY < pageSize.getHeight(); posY = posY + textWidth * 1.35f){
                canvas.showTextAligned(paragraph, posX, posY, documentEvent.getDocument().getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.MIDDLE, waterMarkEvent.getRadAngle());
            }
        }
        canvas.close();
        pdfCanvas.restoreState();
    }
}
