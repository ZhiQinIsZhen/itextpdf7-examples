package com.lyz.service.pdf.core.handler;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.lyz.service.pdf.util.PdfFontUtil;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 16:03
 */
public class PageSizeEventHandler implements IEventHandler {

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle rectangle = page.getPageSize();
        //add font
        PdfFont font = PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Medium);
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        pdfCanvas.beginText()
                .setFontAndSize(font, 9)
                .moveText(rectangle.getWidth() / 2, 25)
                .showText(String.valueOf(pdfDoc.getNumberOfPages()))
                .endText();
        pdfCanvas.release();
    }
}
