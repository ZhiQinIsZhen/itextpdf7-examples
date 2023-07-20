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
        Paragraph paragraph = new Paragraph(pageText)
                .setFont(font)
                .setFontSize(pageSizeEvent.getFontSize())
                .setFontColor(new DeviceRgb(90,90,90));
        PdfExtGState gs = new PdfExtGState().setFillOpacity(pageSizeEvent.getOpacity());
        pdfCanvas.saveState();
        pdfCanvas.setExtGState(gs);
        Canvas canvas = new Canvas(pdfCanvas, documentEvent.getDocument().getDefaultPageSize());
        canvas.showTextAligned(paragraph, (rectangle.getWidth() - width) / 2, 25, documentEvent.getDocument().getNumberOfPages(), TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);
        canvas.close();
        pdfCanvas.restoreState();
    }
}
