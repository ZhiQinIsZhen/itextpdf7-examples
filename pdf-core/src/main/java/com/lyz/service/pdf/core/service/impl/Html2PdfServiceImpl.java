package com.lyz.service.pdf.core.service.impl;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import com.lyz.service.pdf.constant.PdfServiceConstant;
import com.lyz.service.pdf.core.context.MyContext;
import com.lyz.service.pdf.core.handler.PageHeaderEventHandler;
import com.lyz.service.pdf.core.handler.PageSizeEventHandler;
import com.lyz.service.pdf.core.handler.WaterMarkEventHandler;
import com.lyz.service.pdf.core.resource.MyResourceRetriever;
import com.lyz.service.pdf.core.resource.MyTagWorkerFactory;
import com.lyz.service.pdf.core.service.Html2PdfService;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import com.lyz.service.pdf.exception.PdfServiceException;
import com.lyz.service.pdf.util.PdfFontUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.File;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 16:07
 */
@Slf4j
@Service
@EnableConfigurationProperties({ThymeleafProperties.class})
public class Html2PdfServiceImpl implements Html2PdfService, ApplicationContextAware {

    private final ThymeleafProperties properties;
    private ApplicationContext applicationContext;

    public Html2PdfServiceImpl(ThymeleafProperties properties) {
        this.properties = properties;
    }

    /**
     * html模板转化为pdf
     *
     * @param fileName 文件名
     * @param template html模板
     * @param context 上下文
     * @return 文件地址
     */
    @Override
    public String process(String fileName, String template, MyContext context) {
        long startTime = System.currentTimeMillis();
        //创建模板引擎
        ITemplateEngine templateEngine = this.createTemplateEngine();
        String html = templateEngine.process(template, context);
        log.info("解析html模板文件耗时: {}ms", System.currentTimeMillis() - startTime);
        String pdfAbsolutePath = null;
        try {
            File file = FileUtil.createTempFile(fileName, PdfServiceConstant.PDF_POSTFIX);
            pdfAbsolutePath = file.getAbsolutePath();
            WriterProperties writerProperties = new WriterProperties();
            writerProperties.setFullCompressionMode(true);
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfAbsolutePath, writerProperties));
            pdfDocument.setDefaultPageSize(PageSize.A3);
            //添加事件
            this.addEventHandler(pdfDocument, context);
            //创建转化配置
            ConverterProperties converterProperties = this.createConverter();
            converterProperties.setTagWorkerFactory(new MyTagWorkerFactory(pdfDocument));
            Document document = HtmlConverter.convertToDocument(html, pdfDocument, converterProperties);
            document.close();
            pdfDocument.close();
        } catch (Exception e) {
            log.info("pdf file create error, fileName : {}", fileName, e);
            throw new PdfServiceException(PdfExceptionCodeEnum.CREATE_PDF_FAIL);
        }
        log.info("生成pdf文件耗时: {}ms", System.currentTimeMillis() - startTime);
        return pdfAbsolutePath;
    }

    /**
     * 创建模板引擎
     *
     * @return spring模板引擎
     */
    private ITemplateEngine createTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        //资源模板解析器
        templateEngine.setTemplateResolver(this.createTemplateResolver());
        templateEngine.setEnableSpringELCompiler(properties.isEnableSpringElCompiler());
        templateEngine.setRenderHiddenMarkersBeforeCheckboxes(properties.isRenderHiddenMarkersBeforeCheckboxes());
        templateEngine.addDialect(new Java8TimeDialect());
        return templateEngine;
    }

    /**
     * 创建资源模板解析器
     *
     * @return 资源模板解析器
     */
    private ITemplateResolver createTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(properties.getPrefix());
        resolver.setSuffix(properties.getSuffix());
        resolver.setTemplateMode(properties.getMode());
        if (Objects.nonNull(properties.getEncoding())) {
            resolver.setCharacterEncoding(properties.getEncoding().name());
        }
        resolver.setCacheable(properties.isCache());
        resolver.setCheckExistence(properties.isCheckTemplate());
        if (Objects.nonNull(properties.getTemplateResolverOrder())) {
            resolver.setOrder(properties.getTemplateResolverOrder());
        }
        return resolver;
    }

    /**
     * 添加事件
     *
     * @param pdfDocument pdf文档
     * @param context 上下文
     */
    private void addEventHandler(PdfDocument pdfDocument, MyContext context) {
        //页眉事件
        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, new PageHeaderEventHandler(context.getPhEvent()));
        //页码事件
        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, new PageSizeEventHandler(context.getPsEvent()));
        //水印事件
        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, new WaterMarkEventHandler(context.getWmEvent()));
    }

    /**
     * 创建转化配置
     *
     * @return 转化配置
     */
    private ConverterProperties createConverter() {
        String baseUri = properties.getPrefix().replace("classpath:/", "");
        ConverterProperties properties = new ConverterProperties();
        properties.setImmediateFlush(false);
        properties.setBaseUri(baseUri);
        properties.setResourceRetriever(new MyResourceRetriever(baseUri));
        properties.setFontProvider(new FontProvider(PdfFontUtil.getFontSet(), PdfFontUtil.FontEnum.PingFang_Regular.getFont()));
        return properties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
