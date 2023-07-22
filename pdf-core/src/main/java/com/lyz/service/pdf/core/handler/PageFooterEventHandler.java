package com.lyz.service.pdf.core.handler;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.lyz.service.pdf.core.context.PdfContext;
import com.lyz.service.pdf.core.event.PageFooterEvent;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import com.lyz.service.pdf.exception.PdfServiceException;
import com.lyz.service.pdf.util.PdfFontUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Desc:页脚事件
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/20 16:10
 */
@Slf4j
public class PageFooterEventHandler implements IEventHandler {

    private final PageFooterEvent pageFooterEvent;

    public PageFooterEventHandler(PageFooterEvent pageFooterEvent) {
        this.pageFooterEvent = pageFooterEvent;
    }

    @Override
    public void handleEvent(Event event) {
        if (Objects.isNull(pageFooterEvent) || StringUtils.isBlank(pageFooterEvent.getContext())) {
            return;
        }
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        int pageNumber = pdfDocument.getNumberOfPages();
        if (PdfContext.getCover() || pageNumber < pageFooterEvent.getStartPageNumber()) {
            return;
        }
        if (pageNumber > PdfContext.getTotalPage()) {
            return;
        }
        PdfPage page = documentEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        Canvas canvas = new Canvas(page, pageSize);
        try {
            Div footerDiv = new Div()
                    .setHeight(pageFooterEvent.getHeight())
                    .setPaddingTop(10.5f)
                    .setBackgroundColor(new DeviceRgb(255, 128, 71), pageFooterEvent.getOpacity())
                    .setFixedPosition(0,0, pageSize.getWidth());
            Paragraph paragraph = new Paragraph(pageFooterEvent.getContext())
                    .setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Medium))
                    .setMargin(0f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFixedLeading(13f)
                    .setFontSize(pageFooterEvent.getFontSize())
                    .setFontColor(new DeviceRgb(255, 128, 71));
            footerDiv.add(paragraph);
            PdfContext.putFooterDivHeight(pageFooterEvent.getHeight() + footerDiv.getPaddingTop().getValue());
            canvas.add(footerDiv);
        } catch (Exception e) {
            log.info("页脚事件失败", e);
            throw new PdfServiceException(PdfExceptionCodeEnum.PAGE_FOOTER_FAIL);
        } finally {
            canvas.close();
        }
    }
}
