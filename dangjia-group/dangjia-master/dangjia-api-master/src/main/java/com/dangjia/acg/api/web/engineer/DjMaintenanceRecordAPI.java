package com.dangjia.acg.api.web.engineer;

import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * author: wk
 * Date: 13/12/2019
 * Time: 上午 9:52
 */
@FeignClient("dangjia-service-master")
@Api(value = "维保接口", description = "维保接口")
public interface DjMaintenanceRecordAPI {

    @PostMapping(value = "web/engineer/querySkillsCertificationWaitingList")
    @ApiOperation(value = "查询质保审核列表", notes = "查询质保审核列表")
    ServerResponse queryDjMaintenanceRecordList(@RequestParam("pageDTO") PageDTO pageDTO,
                                                @RequestParam("searchKey") String searchKey);

    @PostMapping(value = "web/engineer/queryDjMaintenanceRecordDetail")
    @ApiOperation(value = "查询质保审核详情", notes = "查询质保审核详情")
    ServerResponse queryDjMaintenanceRecordDetail(@RequestParam("id") String id);

    @PostMapping(value = "web/engineer/setDjMaintenanceRecord")
    @ApiOperation(value = "处理质保审核", notes = "处理质保审核")
    ServerResponse setDjMaintenanceRecord(@RequestParam("id") String id,
                                          @RequestParam("state") Integer state);
}
