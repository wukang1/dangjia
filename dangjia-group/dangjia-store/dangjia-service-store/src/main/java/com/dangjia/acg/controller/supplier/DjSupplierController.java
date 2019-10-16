package com.dangjia.acg.controller.supplier;

import com.dangjia.acg.api.supplier.DjSupplierAPI;
import com.dangjia.acg.common.annotation.ApiMethod;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.modle.supplier.DjSupplier;
import com.dangjia.acg.service.supplier.DjSupplierServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * author: wk
 * Date: 8/10/2019
 * Time: 下午 2:19
 */
@RestController
public class DjSupplierController implements DjSupplierAPI {

    @Autowired
    private DjSupplierServices djSupplierServices;


    @Override
    @ApiMethod
    public ServerResponse updateBasicInformation(HttpServletRequest request, DjSupplier djSupplier) {
        return djSupplierServices.updateBasicInformation(djSupplier);
    }

    @Override
    @ApiMethod
    public ServerResponse querySupplyList(HttpServletRequest request, PageDTO pageDTO, String supId, String searchKey) {
        return djSupplierServices.querySupplyList(pageDTO, supId, searchKey);
    }

    @Override
    @ApiMethod
    public ServerResponse querySupplierGoods(HttpServletRequest request, PageDTO pageDTO, String supId) {
        return djSupplierServices.querySupplierGoods(pageDTO,supId);
    }

    @Override
    @ApiMethod
    public ServerResponse queryDjSupplierByShopIdPage(HttpServletRequest request, PageDTO pageDTO, String keyWord, String applicationStatus, String shopId) {
        return djSupplierServices.queryDjSupplierByShopIdPage(request,pageDTO,keyWord,applicationStatus,shopId);
    }

    @Override
    @ApiMethod
    public ServerResponse queryDjSupplierByShopID(HttpServletRequest request,String keyWord, String applicationStatus, String shopId) {
        return djSupplierServices.queryDjSupplierByShopID(request,keyWord,applicationStatus,shopId);
    }


    @Override
    @ApiMethod
    public ServerResponse getDjSupplierByID(HttpServletRequest request, String id,String shopId) {
        return djSupplierServices.getDjSupplierByID(request,id,shopId);
    }

    @Override
    @ApiMethod
    public ServerResponse setDjSupplierPass(HttpServletRequest request, String id, String applicationStatus) {
        return djSupplierServices.setDjSupplierPass(request,id,applicationStatus);
    }

    @Override
    @ApiMethod
    public ServerResponse setDjSupplierReject(HttpServletRequest request, String id, String applicationStatus, String failReason) {
        return djSupplierServices.setDjSupplierReject(request,id,applicationStatus,failReason);
    }

}
