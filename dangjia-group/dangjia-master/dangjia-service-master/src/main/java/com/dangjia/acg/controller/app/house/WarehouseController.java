package com.dangjia.acg.controller.app.house;

import com.dangjia.acg.api.app.house.WarehouseAPI;
import com.dangjia.acg.common.annotation.ApiMethod;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.modle.basics.Product;
import com.dangjia.acg.service.house.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WarehouseController implements WarehouseAPI {

    @Autowired
    private WarehouseService warehouseService;

    @Override
    @ApiMethod
    public ServerResponse checkWarehouseSurplus(String userToken,  String houseId){
        return warehouseService.checkWarehouseSurplus(userToken, houseId);
    }

    /**
     * 购买的材料
     */
    @Override
    @ApiMethod
    public ServerResponse warehouseList(String userToken, PageDTO pageDTO, String houseId, String categoryId, String name, String type) {
        return warehouseService.warehouseList(userToken, pageDTO, houseId, categoryId, name, type);
    }

    /**
     * 购买的材料
     */
    @Override
    @ApiMethod
    public ServerResponse warehouseGmList(HttpServletRequest request, String userToken, String houseId, String name, String type) {
        return warehouseService.warehouseGmList(request, userToken, houseId, name, type);
    }

    @Override
    public ServerResponse editProductData(String cityId, String  productJson){
        return warehouseService.editProductData(cityId, productJson);
    }

    /**
     * 查询指定已购买的商品仓库明细
     * @param houseId 工地ID
     * @param gid 商品ID
     * @param type 类型：  0 = 材料/包工包料   1=人工
     * @return
     *
     */
    @Override
    @ApiMethod
    public ServerResponse getWarehouseData(String houseId, String gid, Integer type){
        return warehouseService.getWarehouseData(houseId, gid,type);
    }
}
