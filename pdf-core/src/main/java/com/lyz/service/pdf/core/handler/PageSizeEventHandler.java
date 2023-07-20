package com.lyz.service.pdf.core.handler;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.util.PdfFontUtil;

import java.util.Objects;

/**
 * 注释:页码事件
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 16:03
 */
public class PageSizeEventHandler implements IEventHandler {

    private final PageSizeEvent pageSizeEvent;

    public PageSizeEventHandler(PageSizeEvent pageSizeEvent) {
        this.pageSizeEvent = pageSizeEvent;
    }

    @Override
    public void handleEvent(Event event) {
        if (Objects.isNull(pageSizeEvent)) {
            return;
        }
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        int pageNumber = pdfDocument.getNumberOfPages();
        if (pageNumber < pageSizeEvent.getStartPageNumber()) {
            return;
        }
        PdfPage page = documentEvent.getPage();
        Rectangle rectangle = page.getPageSize();
        //add font
        PdfFont font = PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Medium);
        String pageText = pageSizeEvent.getPageSizeText(pageNumber);
        float width = font.getWidth(pageText, pageSizeEvent.getFontSize());
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDocument);
        pdfCanvas.beginText()
                .setFontAndSize(font, pageSizeEvent.getFontSize())
                .moveText((rectangle.getWidth() - width) / 2, 25)
                .showText(pageText)
                .endText();
        pdfCanvas.release();
    }
}
