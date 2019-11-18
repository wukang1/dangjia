package com.dangjia.acg.api.app.deliver;

import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.modle.deliver.ProductChange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * author: Yinjianbo
 * Date: 2019-5-11
 */
@FeignClient("dangjia-service-master")
@Api(value = "商品换货操作", description = "商品换货操作")
public interface ProductChangeAPI {


    @PostMapping("app/deliver/productChange/insertProductChange")
    @ApiOperation(value = "添加更换商品", notes = "添加更换商品")
    ServerResponse insertProductChange(HttpServletRequest request, String userToken, String houseId, String srcProductId, String destProductId, Double srcSurCount, Integer productType);

    @PostMapping("app/deliver/productChange/queryChangeByHouseId")
    @ApiOperation(value = "根据houseId查询更换商品列表", notes = "根据houseId查询更换商品列表")
    ServerResponse queryChangeByHouseId(HttpServletRequest request, String userToken, String houseId);

    @PostMapping("app/deliver/productChange/applyProductChange")
    @ApiOperation(value = "申请换货", notes = "申请换货")
    ServerResponse applyProductChange(HttpServletRequest request,String houseId);

    @PostMapping("app/deliver/productChange/productSure")
    @ApiOperation(value = "确定", notes = "确定")
    ServerResponse productSure(HttpServletRequest request, String changeItemList, String orderId);

    @PostMapping("app/deliver/productChangeOrder/queryOrderByHouseId")
    @ApiOperation(value = "根据houseId查询更换商品订单", notes = "根据houseId查询更换商品订单")
    ServerResponse queryOrderByHouseId(HttpServletRequest request, String houseId);

    @PostMapping("app/deliver/productChangeOrder/orderBackFun")
    @ApiOperation(value = "补退差价回调", notes = "补退差价回调")
    ServerResponse orderBackFun(HttpServletRequest request, String id);

    @PostMapping("web/product/change/goods")
    @ApiOperation(value = "补退差价回调", notes = "补退差价回调")
    List<ProductChange> queryChangeDetail(String houseId);
}
