package com.dangjia.acg.controller.config;

import com.dangjia.acg.api.config.ConfigMessageAPI;
import com.dangjia.acg.common.annotation.ApiMethod;
import com.dangjia.acg.common.enums.AppType;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.modle.config.ConfigMessage;
import com.dangjia.acg.service.config.ConfigMessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * author: qiyuxiang
 * Date: 2018/11/07
 * Time: 16:16
 */
@RestController
public class ConfigMessageController implements ConfigMessageAPI {

    @Autowired
    private ConfigMessageService configMessageService;


    /**
     * 获取所有公告消息
     *
     * @param configMessage
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse getConfigMessages(HttpServletRequest request, PageDTO pageDTO, ConfigMessage configMessage) {
        return configMessageService.getConfigMessages(request, pageDTO, configMessage);
    }

    /**
     * 获取所有公告消息(web端列表)
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse queryConfigMessages(HttpServletRequest request, PageDTO pageDTO) {
        return configMessageService.queryConfigMessages(request, pageDTO);
    }


    /**
     * 新增公告消息
     *
     * @param configMessage
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse addConfigMessage(HttpServletRequest request, ConfigMessage configMessage) {
        return configMessageService.addConfigMessage(configMessage);
    }

    @Override
    @ApiMethod
    public ServerResponse addConfigMessage(String memberId, String title, String alert, int type, String data) {
        return configMessageService.addConfigMessage(AppType.SALE,memberId,title,alert,type,data);
    }

    /**
     *  业主退货退款，消息推送
     * @param memberId
     * @param title
     * @param alert
     * @param targetType
     * @param typeText
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse addRefundConfigMessage(HttpServletRequest request,AppType appType,String memberId,String targetType,
                                                 String title,String alert,String typeText){
        return configMessageService.addConfigMessage(request,AppType.ZHUANGXIU,memberId,title,alert,targetType,typeText);
    }
}
