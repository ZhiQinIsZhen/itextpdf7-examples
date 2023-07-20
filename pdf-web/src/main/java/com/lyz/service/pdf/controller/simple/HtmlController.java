package com.lyz.service.pdf.controller.simple;

import com.lyz.service.pdf.core.context.MyContext;
import com.lyz.service.pdf.core.event.PageSizeEvent;
import com.lyz.service.pdf.core.event.WaterMarkEvent;
import com.lyz.service.pdf.core.service.Html2PdfService;
import com.lyz.service.pdf.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/9/29 11:22
 */
@Api(tags = "Html demo")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/simple/html")
public class HtmlController {

    @Resource
    private Html2PdfService html2PdfService;

    @ApiOperation("生成PDF文件-html转化PDF")
    @PostMapping("/create")
    public Result<String> create(@ApiParam(name = "filename")
            @Valid @NotBlank(message = "文件名不能为空")
            @RequestParam(value = "filename", defaultValue = "") String filename) {
        MyContext context = new MyContext();
        context.setPsEvent(new PageSizeEvent());
        WaterMarkEvent waterMarkEvent = new WaterMarkEvent();
        waterMarkEvent.setWaterMark("杭州至秦科技有限公司");
        context.setWmEvent(waterMarkEvent);
        context.setVariable("name", UUID.randomUUID().toString());
        String path = html2PdfService.process(filename, "html/aa", context);
        return Result.success(path);
    }
}
