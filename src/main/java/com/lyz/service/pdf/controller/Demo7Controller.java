package com.lyz.service.pdf.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.lyz.service.pdf.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "demo7")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/demo7")
public class Demo7Controller {

    @ApiOperation("生成PDF文件")
    @PostMapping("/create")
    public Result<Boolean> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件生成地址不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(filename));
        return Result.success(Boolean.TRUE);
    }
}
