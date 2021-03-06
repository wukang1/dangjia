package com.dangjia.acg.controller.app.worker;

import com.dangjia.acg.api.app.worker.EvaluateAPI;
import com.dangjia.acg.common.annotation.ApiMethod;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.modle.worker.Evaluate;
import com.dangjia.acg.service.worker.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * author: Ronalcheng
 * Date: 2018/11/27 0027
 * Time: 14:29
 */
@RestController
public class EvaluateController implements EvaluateAPI {

    @Autowired
    private EvaluateService evaluateService;
    /**
     * 获取积分记录
     * @param userToken
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse queryWorkIntegral(HttpServletRequest request, PageDTO pageDTO, String userToken){
        return evaluateService.queryWorkIntegral(request,pageDTO,userToken);
    }
    @Override
    @ApiMethod
    public ServerResponse queryEvaluates(HttpServletRequest request,  String userToken, Evaluate evaluate){
        return evaluateService.queryEvaluates(request,userToken,evaluate);
    }

    @Override
    @ApiMethod
    public ServerResponse checkNo(String userToken,String houseFlowApplyId,String content){
        return evaluateService.checkNo(houseFlowApplyId,content);
    }

    @Override
    @ApiMethod
    public ServerResponse materialRecord(String userToken,String houseFlowApplyId,String content,Integer star, String productArr,String imageList,String latitude,String longitude){
        return evaluateService.materialRecord(houseFlowApplyId,content,star,productArr,  imageList,latitude, longitude);
    }

    @Override
    @ApiMethod
    public ServerResponse checkOk(String userToken,String houseFlowApplyId,String content,Integer star,String imageList,String latitude,String longitude){
        return evaluateService.checkOk(houseFlowApplyId,content,star, imageList, latitude, longitude);
    }

    @Override
    @ApiMethod
    public ServerResponse saveEvaluateSupervisor(String userToken, String houseFlowApplyId,String content,Integer star,String onekey){
        return evaluateService.saveEvaluateSupervisor( userToken,houseFlowApplyId,content,star,false, onekey);
    }

    @Override
    @ApiMethod
    public ServerResponse saveEvaluate(String userToken,String houseFlowApplyId,String wContent,Integer wStar
            ,String sContent, Integer sStar){
        return evaluateService.saveEvaluate(houseFlowApplyId,wContent,wStar,sContent,sStar,false);
    }
}
