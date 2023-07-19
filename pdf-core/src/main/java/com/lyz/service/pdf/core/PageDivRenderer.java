package com.lyz.service.pdf.core;

import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.renderer.DivRenderer;
import com.itextpdf.layout.renderer.IRenderer;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 14:25
 */
public class PageDivRenderer extends DivRenderer {

    public PageDivRenderer(Div modelElement) {
        super(modelElement);
    }

    @Override
    public IRenderer getNextRenderer() {
        return new PageDivRenderer((Div) modelElement);
    }

    @Override
    public LayoutResult layout(LayoutContext layoutContext) {
        LayoutResult layout = super.layout(layoutContext);
        // 获得页码
        int pageNumber = layoutContext.getArea().getPageNumber();
        Object id = getProperty(Property.ID);
        if (id != null) {
//            Dire toc = GenPdfContext.getTocById((String) id);
//            if (toc != null && toc.getPageNumber()<1) {
//                toc.setPageNumber(pageNumber);
//            }
        }
        return layout;
    }
}
