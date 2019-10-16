package com.dangjia.acg.api.delivery;

import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * author: wk
 * Date: 14/10/2019
 * Time: 上午 10:32
 */
@Api(description = "发货退货单接口")
@FeignClient("dangjia-service-bill")
public interface DjDeliveryReturnSlipAPI {

    @PostMapping("/delivery/djDeliveryReturnSlip/querySupplyTaskList")
    @ApiOperation(value = "供货任务列表", notes = "供货任务列表")
    ServerResponse querySupplyTaskList(@RequestParam("request") HttpServletRequest request,
                                       @RequestParam("pageDTO") PageDTO pageDTO,
                                       @RequestParam("supId") String supId,
                                       @RequestParam("searchKey") String searchKey,
                                       @RequestParam("invoiceStatus") String invoiceStatus);

    @PostMapping("/delivery/djDeliveryReturnSlip/setDeliveryTask")
    @ApiOperation(value = "处理供货任务", notes = "处理供货任务")
    ServerResponse setDeliveryTask(@RequestParam("request") HttpServletRequest request,
                                   @RequestParam("id") String id,
                                   @RequestParam("invoiceStatus") String invoiceStatus);

    @PostMapping("/delivery/djDeliveryReturnSlip/querySupplierSettlementManagement")
    @ApiOperation(value = "供应商结算管理", notes = "供应商结算管理")
    ServerResponse querySupplierSettlementManagement(@RequestParam("request") HttpServletRequest request,
                                                     @RequestParam("supId") String supId,
                                                     @RequestParam("pageDTO") PageDTO pageDTO,
                                                     @RequestParam("applyState") Integer applyState);

    @PostMapping("/delivery/djDeliveryReturnSlip/querySupplierSettlementList")
    @ApiOperation(value = "供应商结算列表", notes = "供应商结算列表")
    ServerResponse querySupplierSettlementList(@RequestParam("request") HttpServletRequest request,
                                               @RequestParam("supId") String supId,
                                               @RequestParam("shopId") String shopId,
                                               @RequestParam("applyState") Integer applyState);
}