package com.dangjia.acg.api.web.repair;

import com.dangjia.acg.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author: Ronalcheng
 * Date: 2018/12/11 0011
 * Time: 11:41
 */
@FeignClient("dangjia-service-master")
@Api(value = "Web端补退人工", description = "Web端补退人工")
public interface WebMendWorkerAPI {

    @RequestMapping(value = "web/repair/webMendWorker/checkWorkerBackState")
    @ApiOperation(value = "审核退人工单", notes = "审核退人工单")
    ServerResponse checkWorkerBackState(@RequestParam("mendOrderId")String mendOrderId, @RequestParam("state")int state);

    /**
     * 退人工审核状态
     * 0生成中,1工匠审核中，2工匠审核不通过，3工匠审核通过即平台审核中，4平台不同意，5平台审核通过,6管家取消
     */
    @RequestMapping(value = "web/repair/webMendWorker/workerBackState")
    @ApiOperation(value = "查询退人工", notes = "查询退人工")
    ServerResponse workerBackState(@RequestParam("houseId")String houseId);

    @RequestMapping(value = "web/repair/webMendWorker/checkWorkerOrderState")
    @ApiOperation(value = "审核补人工", notes = "审核补人工")
    ServerResponse checkWorkerOrderState(@RequestParam("mendOrderId")String mendOrderId,@RequestParam("state")int state);

    @RequestMapping(value = "web/repair/webMendWorker/mendWorkerList")
    @ApiOperation(value = "人工单明细", notes = "人工单明细")
    ServerResponse mendWorkerList(@RequestParam("mendOrderId")String mendOrderId);

    /**
     * 补人工审核状态
     * 0生成中,1工匠审核中，2工匠不同意，3工匠同意即平台审核中，4平台不同意,5平台同意即待业主支付，6业主已支付，7业主不同意, 8管家取消
     */
    @RequestMapping(value = "web/repair/webMendWorker/workerOrderState")
    @ApiOperation(value = "补人工单列表", notes = "补人工单列表")
    ServerResponse workerOrderState(@RequestParam("houseId")String houseId);
}
