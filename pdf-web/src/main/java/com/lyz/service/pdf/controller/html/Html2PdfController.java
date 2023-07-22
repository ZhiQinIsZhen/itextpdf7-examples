package com.lyz.service.pdf.controller.html;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.lyz.service.pdf.constant.PdfServiceConstant;
import com.lyz.service.pdf.core.context.MyContext;
import com.lyz.service.pdf.core.event.*;
import com.lyz.service.pdf.core.handler.PageFooterEventHandler;
import com.lyz.service.pdf.core.handler.PageHeaderEventHandler;
import com.lyz.service.pdf.core.handler.PageSizeEventHandler;
import com.lyz.service.pdf.core.service.Html2PdfService;
import com.lyz.service.pdf.result.Result;
import com.lyz.service.pdf.util.DateUtil;
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

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/7/21 15:06
 */
@Api(tags = "html")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败")
})
@Slf4j
@Validated
@RestController
@RequestMapping("/pdf/html")
public class Html2PdfController {

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

        PageHeaderEvent event = new PageHeaderEvent();
        event.setStartPageNumber(1);
        event.setContext("这是一个页眉测试Demo");
        event.setBkImage(PdfServiceConstant.ROOT_TEMPLATE_IMAGE + "ph/ph1.png");
        event.setTime(DateUtil.currentDate());
        event.setFlag("这个一个Flag");
        event.setLogoImage(PdfServiceConstant.ROOT_TEMPLATE_IMAGE + "logo/logo1.png");
        context.setPhEvent(event);

        PageFooterEvent pageFooterEvent = new PageFooterEvent();
        pageFooterEvent.setContext("这是一个页脚，是不是很棒！！！！！");
        context.setPfEvent(pageFooterEvent);

        PageSizeEvent pageSizeEvent = new PageSizeEvent();
        context.setPsEvent(pageSizeEvent);

        CoverEvent coverEvent = new CoverEvent();
        coverEvent.setCoverImage(PdfServiceConstant.ROOT_TEMPLATE_IMAGE + "cover/cover.jpeg");
        context.setCoverEvent(coverEvent);

        BackCoverEvent backCoverEvent = new BackCoverEvent();
        backCoverEvent.setBackCoverImage(PdfServiceConstant.ROOT_TEMPLATE_IMAGE + "cover/cover1.jpg");
        context.setBackCoverEvent(backCoverEvent);

        context.setVariable("name", "这是一个封面！！！！！");
        String path = html2PdfService.process(filename, "html/aa", context);
        return Result.success(path);
    }
}
