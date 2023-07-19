package com.lyz.service.pdf.core;

import com.itextpdf.styledxmlparser.resolver.resource.DefaultResourceRetriever;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/19 11:42
 */
@Slf4j
public class MyResourceRetriever extends DefaultResourceRetriever {

    private final String classPathPrefix;

    private final RestTemplate restTemplate;

    public MyResourceRetriever(String classPathPrefix) {
        this.classPathPrefix = classPathPrefix;
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(600000);
        httpRequestFactory.setConnectTimeout(600000);
        httpRequestFactory.setReadTimeout(600000);
        this.restTemplate = new RestTemplate(httpRequestFactory);
    }

    @Override
    public InputStream getInputStreamByUrl(URL url) throws IOException {
        String uri = url.toExternalForm();
        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            try {
                Resource resource = restTemplate.getForObject(url.toURI(), Resource.class);
                return Objects.isNull(resource) ? default404InputStream() : resource.getInputStream();
            } catch (Exception e) {
                log.error("获取资源文件失败", e);
                return default404InputStream();
            }
        }
        return new ClassPathResource(uri.substring(uri.lastIndexOf(classPathPrefix))).getInputStream();
    }

    private InputStream default404InputStream() throws IOException {
        //返回404图片
        return new ClassPathResource("templates/image/404.png").getInputStream();
    }
}
