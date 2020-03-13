package com.dangjia.acg.api.delivery;

import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Api(description = "所有订单表(装修所需的订单流水表)接口")
@FeignClient("dangjia-service-bill")
public interface DjDeliverOrderAPI {

    @PostMapping("app/design/getDesignImag")
    @ApiOperation(value = "获取设计图", notes = "获取设计图")
    ServerResponse getDesignImag(@RequestParam("request") HttpServletRequest request,
                                 @RequestParam("houseId") String houseId,
                                 @RequestParam("type") Integer type);

    @PostMapping("app/design/getDesignInfo")
    @ApiOperation(value = "获取设计验收过程", notes = "获取设计验收过程")
    ServerResponse getDesignInfo(@RequestParam("request") HttpServletRequest request,
                                 @RequestParam("houseId") String houseId);

    @PostMapping("app/design/getActuaryInfo")
    @ApiOperation(value = "获取精算信息", notes = "获取精算信息")
    ServerResponse getActuaryInfo(@RequestParam("request") HttpServletRequest request,
                                  @RequestParam("houseId") String houseId);

    @PostMapping("app/design/getCollectInfo")
    @ApiOperation(value = "查询验收过程", notes = "查询验收过程")
    ServerResponse getCollectInfo(@RequestParam("request") HttpServletRequest request,
                                  @RequestParam("houseId") String houseId);


    /**
     * 店铺--订单管理--列表
     * @param request
     * @param pageDTO 查询分页
     * @param userId 用户ID
     * @param cityId 城市ID
     * @param orderKey 查询内容（房屋地址/电话/支付订单号/订单号）
     * @param state 状态：3已支付
     * @return
     */
    @PostMapping("web/design/queryOrderInfo")
    @ApiOperation(value = "店铺--订单管理--列表", notes = "店铺--订单管理--列表")
    ServerResponse queryOrderInfo(@RequestParam("request") HttpServletRequest request,
                                  @RequestParam("pageDTO") PageDTO pageDTO,
                                  @RequestParam("userId") String userId,
                                  @RequestParam("cityId") String cityId,
                                  @RequestParam("orderKey") String orderKey,
                                  @RequestParam("state") Integer state);

    @PostMapping("web/design/queryOrderFineInfo")
    @ApiOperation(value = "店铺--订单管理--查询订单详情", notes = "店铺--订单管理--查询订单详情")
    ServerResponse queryOrderFineInfo(@RequestParam("request") HttpServletRequest request,
                                      @RequestParam("pageDTO") PageDTO pageDTO,
                                      @RequestParam("orderId") String orderId);


    @PostMapping("app/order/queryDeliverOrderListByStatus")
    @ApiOperation(value = "根据订单状态查询订单详情列表(全部订单、待付款、待发货)", notes = "根据订单状态查询订单详情（全部订单、待付款、待发货）")
    ServerResponse queryDeliverOrderListByStatus(@RequestParam("pageDTO") PageDTO pageDTO,
                                                 @RequestParam("userToken") String userToken,
                                                 @RequestParam("houseId") String houseId,
                                                 @RequestParam("cityId") String queryId,
                                                 @RequestParam("orderStatus") String orderStatus);

    @PostMapping("app/order/queryDeliverOrderDsdListByStatus")
    @ApiOperation(value = "根据订单状态查询订单详情列表(待收货、已完成)", notes = "根据订单状态查询订单详情（待收货、已完成）")
    ServerResponse queryDeliverOrderDsdListByStatus(@RequestParam("pageDTO") PageDTO pageDTO,
                                                    @RequestParam("userToken") String userToken,
                                                    @RequestParam("houseId") String houseId,
                                                    @RequestParam("cityId") String queryId,
                                                    @RequestParam("orderStatus") String orderStatus);


    @PostMapping("app/deliverOrderItem/deliverOrderItemDetail")
    @ApiOperation(value = "订单详情明细", notes = "订单详情明细")
    ServerResponse deliverOrderItemDetail(@RequestParam("orderId") String orderId, @RequestParam("orderStatus") Integer orderStatus);



    @PostMapping("app/deliverOrderItem/shippingDetail")
    @ApiOperation(value = "订单-发货详情", notes = "订单-发货详情")
    ServerResponse shippingDetail(@RequestParam("orderId") String orderId, @RequestParam("orderStatus") Integer orderStatus);

    @PostMapping("app/deliverOrderItem/stevedorageCostDetail")
    @ApiOperation(value = "订单-搬运费详情", notes = "订单-搬运费详情")
    ServerResponse stevedorageCostDetail(@RequestParam("pageDTO") PageDTO pageDTO, @RequestParam("orderId") String orderId, @RequestParam("orderStatus") Integer orderStatus);

    @PostMapping("app/deliverOrderItem/transportationCostDetail")
    @ApiOperation(value = "订单-运费详情", notes = "订单-运费详情")
    ServerResponse transportationCostDetail(@RequestParam("pageDTO") PageDTO pageDTO, @RequestParam("orderId") String orderId, @RequestParam("orderStatus") Integer orderStatus);


    @PostMapping("app/order/queryDeliverOrderHump")
    @ApiOperation(value = "我的订单,待付款,库存,已取消", notes = "我的订单,待付款,库存,已取消")
    ServerResponse queryDeliverOrderHump(@RequestParam("pageDTO") PageDTO pageDTO,
                                         @RequestParam("houseId") String houseId,
                                         @RequestParam("state") String state,
                                         @RequestParam("userToken") String userToken);
//    /**
//     * 取消订单
//     *
//     * @param userToken
//     * @param orderId
//     * @return
//     */
//    @PostMapping("app/order/cancelBusinessOrderById")
//    @ApiOperation(value = "取消订单", notes = "取消订单")
//    ServerResponse cancelBusinessOrderById(@RequestParam("userToken") String userToken, @RequestParam("orderId") String orderId);
//
//
//    /**
//     * 删除已经购物的订单
//     *
//     * @param userToken
//     * @param orderId
//     * @return
//     */
//    @PostMapping("app/order/delBusinessOrderById")
//    @ApiOperation(value = "删除订单", notes = "删除订单")
//    ServerResponse delBusinessOrderById(@RequestParam("userToken") String userToken, @RequestParam("orderId") String orderId);


    @PostMapping("app/order/queryAppOrderList")
    @ApiOperation(value = "根据订单状态查询订单列表(待收货、已完成)", notes = "根据订单状态查询订单列表（待收货、已完成）")
    ServerResponse queryAppOrderList(@RequestParam("pageDTO") PageDTO pageDTO,
                                     @RequestParam("userToken") String userToken,
                                     @RequestParam("houseId") String houseId,
                                     @RequestParam("cityId") String queryId,
                                     @RequestParam("orderStatus") Integer orderStatus,
                                     @RequestParam("idList") String idList);

    @PostMapping("app/order/queryAppHairOrderList")
    @ApiOperation(value = "查询订单列表（待发货）", notes = "查询订单列表（待发货")
    ServerResponse queryAppHairOrderList(@RequestParam("userToken") String userToken,
                                         @RequestParam("pageDTO") PageDTO pageDTO,
                                         @RequestParam("houseId") String houseId,
                                         @RequestParam("cityId") String cityId);

    @PostMapping("app/order/queryAppHairOrderInFo")
    @ApiOperation(value = "查询订单详情（待发货）", notes = "查询订单详情（待发货")
    ServerResponse queryAppHairOrderInFo(@RequestParam("userToken") String userToken,
                                         @RequestParam("id") String id);


    @PostMapping("app/order/updateAppOrderStats")
    @ApiOperation(value = "待收货（材料）详情 确定收货", notes = "待收货（材料）详情确定收货")
    ServerResponse updateAppOrderStats(@RequestParam("userToken") String userToken,
                                       @RequestParam("lists") String lists,
                                       @RequestParam("id") String id);

    @PostMapping("app/order/refuseAppOrderStats")
    @ApiOperation(value = "待收货（材料）详情 拒绝收货", notes = "待收货（材料）详情 拒绝收货")
    ServerResponse refuseAppOrderStats(@RequestParam("userToken") String userToken,
                                       @RequestParam("id") String id);

    @PostMapping("app/order/installAppOrderStats")
    @ApiOperation(value = "确定安装", notes = "确定安装")
    ServerResponse installAppOrderStats(@RequestParam("userToken") String userToken,
                                        @RequestParam("id") String id);

    @PostMapping("app/order/queryAppOrderInFoList")
    @ApiOperation(value = "app订单详情查询（待收货，待安装，已完成）", notes = "app订单详情查询（待收货，待安装，已完成）")
    ServerResponse queryAppOrderInFoList(@RequestParam("userToken") String userToken,
                                         @RequestParam("pageDTO") PageDTO pageDTO,
                                         @RequestParam("id") String id,
                                         @RequestParam("shippingState") String shippingState);

    @PostMapping("app/order/queryAppOrderWorkerInFoList")
    @ApiOperation(value = "订单详情查询（待收货- 人工）", notes = "订单详情查询（待收货- 人工）")
    ServerResponse queryAppOrderWorkerInFoList(@RequestParam("userToken") String userToken,
                                               @RequestParam("id") String id,
                                               @RequestParam("shippingState") String shippingState);

    @PostMapping("app/order/deleteAppOrder")
    @ApiOperation(value = "删除订单", notes = "删除订单")
    ServerResponse deleteAppOrder(@RequestParam("userToken") String userToken,
                                  @RequestParam("id") String id);

    @PostMapping("app/order/setConfirmReceipt")
    @ApiOperation(value = "待收货列表-确认收货", notes = "收货列表-确认收货")
    ServerResponse setConfirmReceipt(@RequestParam("id") String id);

    @PostMapping("app/order/queryWorkerGoods")
    @ApiOperation(value = "查询订单人工商品", notes = "查询订单人工商品")
    ServerResponse queryWorkerGoods(@RequestParam("houseId") String houseId,
                                    @RequestParam("orderStatus") Integer orderStatus);

    @PostMapping("app/order/queryWorkerGoodsInFo")
    @ApiOperation(value = "查询订单人工商品详情", notes = "查询订单人工商品详情")
    ServerResponse queryWorkerGoodsInFo(@RequestParam("id") String id);

    @PostMapping("app/order/queryCostDetailsAfterCompletion")
    @ApiOperation(value = "完工后-花费明细", notes = "完工后-花费明细")
    ServerResponse queryCostDetailsAfterCompletion(@RequestParam("houseId") String houseId);

}
