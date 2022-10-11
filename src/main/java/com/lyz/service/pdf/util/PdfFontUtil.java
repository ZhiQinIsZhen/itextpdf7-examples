package com.lyz.service.pdf.util;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.lyz.service.pdf.exception.PdfException;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 9:57
 */
@Slf4j
@UtilityClass
public class PdfFontUtil {

    static {
        FontProgramFactory.registerFont(FontEnum.WRYH.path, FontEnum.WRYH.font);
        FontProgramFactory.registerFont(FontEnum.SOURCE_OLD_STYLE.path, FontEnum.SOURCE_OLD_STYLE.font);
        FontProgramFactory.registerFont(FontEnum.SONG_STYLE.path, FontEnum.SONG_STYLE.font);
    }

    /**
     * 创建字体
     *
     * @param fontEnum
     * @return
     */
    public static PdfFont createFont(FontEnum fontEnum) {
        if (Objects.isNull(fontEnum)) {
            throw new NullPointerException("fontEnum is null");
        }
        PdfFont font;
        try {
            font = PdfFontFactory.createFont(FontProgramFactory.createRegisteredFont(fontEnum.font), PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            log.info("create font fail.  ", e);
            throw new PdfException(PdfExceptionCodeEnum.CREATE_PDF_FONT_FAIL);
        }
        return font;
    }

    @AllArgsConstructor
    public enum FontEnum {
        WRYH("WeiRuanYaHei", "微软雅黑", "C:\\code\\liyz\\itextpdf7-examples\\src\\main\\resources\\templates\\font\\WeiRuanYaHei-1.ttf"),
        SOURCE_OLD_STYLE("SourceHanSansOldStyle-Regular", "思源黑体旧字形", "C:\\code\\liyz\\itextpdf7-examples\\src\\main\\resources\\templates\\font\\SourceHanSansOldStyle-Regular.ttf"),
        SONG_STYLE("SongStyle", "简体-宋体", "C:\\code\\liyz\\itextpdf7-examples\\src\\main\\resources\\templates\\font\\simsun.ttf"),
        ;
        @Getter
        private String font;
        @Getter
        private String desc;
        @Getter
        private String path;
    }
}
