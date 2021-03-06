package com.dangjia.acg.api.web.deliver;

import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author: Yinjianbo
 * Date: 2019-5-16
 */
@FeignClient("dangjia-service-master")
@Api(value = "Web端换货", description = "Web端换货")
public interface WebProductChangeAPI {

    @PostMapping(value = "web/deliver/productChange/queryChangeOrderState")
    @ApiOperation(value = "查询换货单列表", notes = "查询换货单列表")
    ServerResponse queryChangeOrderState(@RequestParam("houseId") String houseId,
                                         @RequestParam("pageDTO") PageDTO pageDTO,
                                         @RequestParam("beginDate") String beginDate,
                                         @RequestParam("endDate") String endDate,
                                         @RequestParam("likeAddress") String likeAddress);

    @PostMapping("web/deliver/productChange/queryChangeDetail")
    @ApiOperation(value = "换货详情", notes = "换货详情")
    ServerResponse queryChangeDetail(String orderId, String houseId);

    @PostMapping("web/deliver/productChange/queryPayChangeDetail")
    @ApiOperation(value = "支付订单换货详情", notes = "支付订单换货详情")
    ServerResponse queryPayChangeDetail(String number, String taskId);
}
