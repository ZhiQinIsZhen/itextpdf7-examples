package com.lyz.service.pdf.core.handler;

import cn.hutool.core.math.MathUtil;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.lyz.service.pdf.core.context.PdfContext;
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
        Rectangle pageSize = page.getPageSize();
        Canvas canvas = new Canvas(page, pageSize);
        //add font
        PdfFont font = PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Medium);
        String pageText = pageSizeEvent.getPageSizeText(pageNumber);
        float width = font.getWidth(pageText, pageSizeEvent.getFontSize());
        float bottom = PdfContext.getFooterDivHeight();
        Div pageSizeDiv = new Div()
                .setHeight(pageSizeEvent.getFontSize() + 20f)
                .setPaddingBottom(0f)
                .setFixedPosition(0, bottom, pageSize.getWidth());
        Paragraph paragraph = new Paragraph(pageText)
                .setMargin(0)
                .setMarginTop(2)
                .setFont(font)
                .setFontSize(pageSizeEvent.getFontSize())
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(90,90,90));
        pageSizeDiv.add(paragraph);
        canvas.add(pageSizeDiv).close();
    }
}
