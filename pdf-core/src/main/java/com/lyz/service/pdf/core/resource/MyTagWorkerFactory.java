package com.lyz.service.pdf.core.resource;

import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.styledxmlparser.node.IElementNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 14:20
 */
@Slf4j
public class MyTagWorkerFactory extends DefaultTagWorkerFactory {

    private final PdfDocument pdfDocument;

    public MyTagWorkerFactory(PdfDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
    }

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if (TagConstants.DIV.equals(tag.name())) {
            String display = tag.getStyles() != null ? tag.getStyles().get(CssConstants.DISPLAY) : null;
            String id = tag.getAttribute("id");
            if (StringUtils.isNotBlank(id)) {
                log.info("div id : {}", id);
            }
            if(CssConstants.BLOCK.equalsIgnoreCase(display)){
                return new MyDivTagWorker(tag, context);
            }
        }
        return super.getCustomTagWorker(tag, context);
    }
}
