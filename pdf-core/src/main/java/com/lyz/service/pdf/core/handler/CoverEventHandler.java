package com.lyz.service.pdf.core.handler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.properties.BackgroundImage;
import com.itextpdf.layout.properties.BackgroundSize;
import com.itextpdf.layout.properties.UnitValue;
import com.lyz.service.pdf.core.context.PdfContext;
import com.lyz.service.pdf.core.event.CoverEvent;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import com.lyz.service.pdf.exception.PdfServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

/**
 * 注释:封面事件
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 15:08
 */
@Slf4j
public class CoverEventHandler implements IEventHandler {

    private final CoverEvent coverEvent;

    public CoverEventHandler(CoverEvent coverEvent) {
        this.coverEvent = coverEvent;
    }

    @Override
    public void handleEvent(Event event) {
        if (Objects.isNull(coverEvent) || StringUtils.isBlank(coverEvent.getCoverImage()) || !PdfContext.getCover()) {
            return;
        }
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfPage page = documentEvent.getPage();
        Rectangle pageSize = page.getPageSize();
        Canvas canvas = new Canvas(page, pageSize);
        try {
            //封面图片
            ImageData imageData = ImageDataFactory.create(new ClassPathResource(coverEvent.getCoverImage()).getURL());
            //背景尺寸
            BackgroundSize backgroundSize = new BackgroundSize();
            //铺满
            backgroundSize.setBackgroundSizeToValues(UnitValue.createPercentValue(100), UnitValue.createPercentValue(100));
            BackgroundImage backgroundImage = new BackgroundImage.Builder()
                    .setImage(new PdfImageXObject(imageData))
                    .setBackgroundSize(backgroundSize)
                    .build();
            //定义封面DIV
            Div coverDiv = new Div()
                    .setHeight(pageSize.getHeight())
                    .setWidth(pageSize.getWidth())
                    .setBackgroundImage(backgroundImage);
            canvas.add(coverDiv);
        } catch (Exception e) {
            log.info("封面事件失败", e);
            throw new PdfServiceException(PdfExceptionCodeEnum.COVER_FAIL);
        } finally {
            canvas.close();
        }
    }

}
