package com.lyz.service.pdf.core.handler;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.properties.BackgroundImage;
import com.itextpdf.layout.properties.BackgroundSize;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * 注释:封面事件
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 15:08
 */
@Slf4j
public class CoverEventHandler implements IEventHandler {

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent documentEvent = (PdfDocumentEvent) event;
        PdfDocument document = documentEvent.getDocument();
        if (document.getNumberOfPages() != 1) {
            return;
        }
        PdfPage page = documentEvent.getPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), document);
        pdfCanvas.saveState().rectangle(page.getArtBox().getX(), page.getArtBox().getY(), page.getArtBox().getWidth(), page.getArtBox().getHeight()).fill().restoreState();
        ImageData imageData = null;
        try {
            imageData = ImageDataFactory.create(new ClassPathResource("templates/image/face-bg.jpg").getURL());
        } catch (IOException e) {
            log.error("image create fail, ", e);
        }
        BackgroundSize backgroundSize = new BackgroundSize();
        // 铺满
        backgroundSize.setBackgroundSizeToValues(UnitValue.createPercentValue(100), UnitValue.createPercentValue(100));
        BackgroundImage backgroundImage = new BackgroundImage.Builder()
                .setImage(new PdfImageXObject(imageData))
                .setBackgroundSize(backgroundSize)
                .build();
        Div coverDiv = new Div().setBackgroundImage(backgroundImage);
        new Canvas(pdfCanvas, new Rectangle(page.getPageSize().getLeft(), page.getPageSize().getBottom(),
                page.getPageSize().getWidth(), page.getPageSize().getHeight())).add(coverDiv).close();
    }

}
