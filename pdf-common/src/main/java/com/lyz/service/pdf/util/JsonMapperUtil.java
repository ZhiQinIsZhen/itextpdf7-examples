package com.lyz.service.pdf.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Desc:Json tool
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/3/8 15:12
 */
@UtilityClass
public class JsonMapperUtil {

    private static final ObjectMapper OBJECT_MAPPER = new Jackson2ObjectMapperBuilder().createXmlMapper(false).build();

    @SneakyThrows
    public static String toJSONString(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T readValue(String content, Class<T> clazz) {
        if (StringUtils.isBlank(content) || Objects.isNull(clazz)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(content, clazz);
    }

    @SneakyThrows
    public static <T> T readValue(InputStream inputStream, Class<T> clazz) {
        if (ObjectUtils.anyNull(inputStream, clazz)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(inputStream, clazz);
    }

    @SneakyThrows
    public static void writeValue(OutputStream out, Object value) {
        OBJECT_MAPPER.writeValue(out, value);
    }
}
