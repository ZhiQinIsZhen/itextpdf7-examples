package com.lyz.service.pdf.util;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.font.FontSet;
import com.lyz.service.pdf.exception.PdfExceptionCodeEnum;
import com.lyz.service.pdf.exception.PdfServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * 注释:字体工具类
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 9:57
 */
@Slf4j
@UtilityClass
public class PdfFontUtil {

    private static final FontSet FONT_SET = new FontSet();

    static {
        FontProgramFactory.registerFont(FontEnum.PingFang_ExtraLight.path, FontEnum.PingFang_ExtraLight.font);
        FontProgramFactory.registerFont(FontEnum.PingFang_Light.path, FontEnum.PingFang_Light.font);
        FontProgramFactory.registerFont(FontEnum.PingFang_Regular.path, FontEnum.PingFang_Regular.font);
        FontProgramFactory.registerFont(FontEnum.PingFang_Medium.path, FontEnum.PingFang_Medium.font);
        FontProgramFactory.registerFont(FontEnum.PingFang_Bold.path, FontEnum.PingFang_Bold.font);
        FontProgramFactory.registerFont(FontEnum.PingFang_Heavy.path, FontEnum.PingFang_Heavy.font);
        FontProgramFactory.registerFont(FontEnum.WeiRuanYaHei_Regular.path, FontEnum.WeiRuanYaHei_Regular.font);

        try {
            FONT_SET.addFont(FontProgramFactory.createRegisteredFont(FontEnum.PingFang_ExtraLight.font), PdfEncodings.IDENTITY_H, FontEnum.PingFang_ExtraLight.font);
            FONT_SET.addFont(FontProgramFactory.createRegisteredFont(FontEnum.PingFang_Light.font), PdfEncodings.IDENTITY_H, FontEnum.PingFang_Light.font);
            FONT_SET.addFont(FontProgramFactory.createRegisteredFont(FontEnum.PingFang_Regular.font), PdfEncodings.IDENTITY_H, FontEnum.PingFang_Regular.font);
            FONT_SET.addFont(FontProgramFactory.createRegisteredFont(FontEnum.PingFang_Medium.font), PdfEncodings.IDENTITY_H, FontEnum.PingFang_Medium.font);
            FONT_SET.addFont(FontProgramFactory.createRegisteredFont(FontEnum.PingFang_Bold.font), PdfEncodings.IDENTITY_H, FontEnum.PingFang_Bold.font);
            FONT_SET.addFont(FontProgramFactory.createRegisteredFont(FontEnum.PingFang_Heavy.font), PdfEncodings.IDENTITY_H, FontEnum.PingFang_Heavy.font);
            FONT_SET.addFont(FontProgramFactory.createRegisteredFont(FontEnum.WeiRuanYaHei_Regular.font), PdfEncodings.IDENTITY_H, FontEnum.WeiRuanYaHei_Regular.font);
        } catch (IOException e) {
            throw new PdfServiceException(PdfExceptionCodeEnum.CREATE_PDF_FONT_FAIL);
        }
    }

    public static FontSet getFontSet() {
        return FONT_SET;
    }

    /**
     * 创建字体
     *
     * @param fontEnum 字体枚举
     * @return 字体
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
            throw new PdfServiceException(PdfExceptionCodeEnum.CREATE_PDF_FONT_FAIL);
        }
        return font;
    }

    @AllArgsConstructor
    public enum FontEnum {
        PingFang_ExtraLight("pingfang_extralight", "苹方 特细", "front/PingFang ExtraLight.ttf"),
        PingFang_Light("pingfang_light", "苹方 细体", "front/PingFang Light.ttf"),
        PingFang_Regular("pingfang_regular", "苹方 常规", "front/PingFang Regular.ttf"),
        PingFang_Medium("pingfang_medium", "苹方 中等", "front/PingFang Medium.ttf"),
        PingFang_Bold("pingfang_bold", "苹方 粗体", "front/PingFang Bold.ttf"),
        PingFang_Heavy("pingfang_heavy", "苹方 特粗", "front/PingFang Heavy.ttf"),
        WeiRuanYaHei_Regular("weiruanyahei_regular", "微软雅黑", "front/WeiRuanYaHei-1.ttf"),
        ;
        @Getter
        private final String font;
        @Getter
        private final String desc;
        @Getter
        private final String path;
    }
}
