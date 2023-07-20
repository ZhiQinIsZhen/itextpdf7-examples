package com.lyz.service.pdf.core.handler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.*;
import com.lyz.service.pdf.core.event.PageHeaderEvent;
import com.lyz.service.pdf.core.resource.MyResourceRetriever;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import com.lyz.service.pdf.exception.PdfServiceException;
import com.lyz.service.pdf.util.DateUtil;
import com.lyz.service.pdf.util.PdfFontUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

/**
 * Desc:页眉事件
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 17:38
 */
@Slf4j
public class PageHeaderEventHandler implements IEventHandler {

    private final PageHeaderEvent pageHeaderEvent;

    public PageHeaderEventHandler(PageHeaderEvent pageHeaderEvent) {
        this.pageHeaderEvent = pageHeaderEvent;
    }

    @Override
    public void handleEvent(Event event) {
        if (Objects.isNull(pageHeaderEvent)) {
            return;
        }
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDocument = documentEvent.getDocument();
        int pageNumber = pdfDocument.getNumberOfPages();
        if (pageNumber < pageHeaderEvent.getStartPageNumber()) {
            return;
        }
        PdfPage page = documentEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        Canvas canvas = new Canvas(page, pageSize);
        try {
            //页眉背景图
            ImageData imageData = ImageDataFactory.create(new ClassPathResource(pageHeaderEvent.getBkImage()).getURL());
            //背景尺寸
            BackgroundSize backgroundSize = new BackgroundSize();
            //铺满
            backgroundSize.setBackgroundSizeToValues(UnitValue.createPercentValue(100), UnitValue.createPercentValue(100));
            BackgroundImage backgroundImage = new BackgroundImage.Builder()
                    .setImage(new PdfImageXObject(imageData))
                    .setBackgroundSize(backgroundSize)
                    .build();
            //定义页眉DIV
            Div headerDiv = new Div().setBackgroundImage(backgroundImage).setHeight(pageHeaderEvent.getHeight());
            //设置Flag文本
            Paragraph flagText = new Paragraph(pageHeaderEvent.getFlag())
                    .setMargin(0)
                    .setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Medium))
                    .setFontSize(14)
                    .setFixedLeading(15);
            //时间文本
            Paragraph timeText = new Paragraph("页眉时间: " + DateUtil.formatDate(pageHeaderEvent.getTime()))
                    .setMargin(0)
                    .setMarginTop(7)
                    .setFont(PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Regular))
                    .setFontSize(10)
                    .setFixedLeading(10)
                    .setOpacity(0.63f);
            //包含文本和时间文本
            Div textDiv = new Div()
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setPaddingRight(59)
                    .setPaddingTop(15)
                    .add(flagText)
                    .add(timeText)
                    .setTextAlignment(TextAlignment.RIGHT);
            //div叠加
            headerDiv.add(textDiv);
            //添加logo图片
            Image logoImage = new Image(ImageDataFactory.create(MyResourceRetriever.getUrl(pageHeaderEvent.getLogoImage())));
            float logoSize = 28f;
            Div logoDiv = new Div()
                    .add(logoImage.setWidth(logoSize).setMaxHeight(logoSize).setBorderRadius(new BorderRadius(3,3)))
                    .setFixedPosition(170.5f, pageSize.getHeight()-(28.5f+17.25f), logoSize);
            headerDiv.add(logoDiv);
            //内容文本
            float contextWidth = 400f;float contextFont = 17f;
            PdfFont pingFangFontHeavy = PdfFontUtil.createFont(PdfFontUtil.FontEnum.PingFang_Heavy);
            if(pingFangFontHeavy.getWidth(pageHeaderEvent.getContext(), contextFont) > contextWidth){
                contextFont = 12f;
            }
            Div corp = new Div()
                    .add(new Paragraph(pageHeaderEvent.getContext())
                            .setFontColor(DeviceRgb.WHITE).setMargin(0)
                            .setFontSize(contextFont)
                            .setFont(pingFangFontHeavy)
                            .setFixedLeading(28.5f))
                    //设置内容DIV的位置，如果没有logo需要靠左边一点
                    .setFixedPosition(StringUtils.isNotBlank(pageHeaderEvent.getLogoImage()) ? 205 : 165,
                            pageSize.getHeight()-(28.5f+17.25f),
                            contextWidth);
            headerDiv.add(corp);
            canvas.add(headerDiv);
        } catch (Exception e) {
            log.info("页眉事件失败", e);
            throw new PdfServiceException(PdfExceptionCodeEnum.PAGE_HEADER_FAIL);
        } finally {
            canvas.close();
        }
    }
}
