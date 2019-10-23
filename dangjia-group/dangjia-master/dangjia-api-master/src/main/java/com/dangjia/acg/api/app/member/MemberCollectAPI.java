package com.dangjia.acg.api.app.member;

import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@FeignClient("dangjia-service-master")
@Api(value = "当家用户收藏接口", description = "当家用户收藏接口")
public interface MemberCollectAPI {


    @PostMapping("member/memberCollect/queryCollectHouse")
    @ApiOperation(value = "查询收藏的工地记录", notes = "查询收藏的工地记录")
    ServerResponse queryCollectHouse(@RequestParam("request") HttpServletRequest request,
                                     @RequestParam("userToken") String userToken,
                                     @RequestParam("pageDTO") PageDTO pageDTO);

    @PostMapping("member/memberCollect/queryCollectGood")
    @ApiOperation(value = "查询收藏的商品记录", notes = "查询收藏的商品记录")
    ServerResponse queryCollectGood(@RequestParam("request") HttpServletRequest request,
                                    @RequestParam("userToken") String userToken,
                                    @RequestParam("pageDTO") PageDTO pageDTO);


    @PostMapping("member/memberCollect/addMemberCollect")
    @ApiOperation(value = "添加收藏", notes = "添加收藏,collectType:0->代表收藏房子 1->代表收藏商品")
    ServerResponse addMemberCollect(@RequestParam("request") HttpServletRequest request,
                                    @RequestParam("userToken") String userToken,
                                    @RequestParam("collectId") String collectId,
                                    @RequestParam("collectType") String collectType);

    @RequestMapping(value = "member/collect/check", method = RequestMethod.POST)
    @ApiOperation(value = "检测是否收藏", notes = "检测是否收藏，collectType:0->代表收藏房子 1->代表收藏商品")
    ServerResponse isMemberCollect(@RequestParam("request") HttpServletRequest request,
                                   @RequestParam("houseId") String houseId,@RequestParam("collectType") String collectType);

    @PostMapping("member/memberCollect/delMemberCollect")
    @ApiOperation(value = "取消收藏", notes = "取消收藏,collectType:0->代表收藏房子 1->代表收藏商品")
    ServerResponse delMemberCollect(@RequestParam("request") HttpServletRequest request,
                                    @RequestParam("userToken") String userToken,
                                    @RequestParam("collectId") String collectId,
                                    @RequestParam("collectType") String collectType);


    @PostMapping("member/memberCollect/queryRelated")
    @ApiOperation(value = "猜你喜欢", notes = "猜你喜欢")
    ServerResponse queryRelated(@RequestParam("request") HttpServletRequest request,
                                @RequestParam("userToken") String userToken);

}

