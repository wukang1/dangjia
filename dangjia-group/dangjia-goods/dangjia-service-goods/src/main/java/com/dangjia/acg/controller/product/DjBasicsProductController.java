package com.dangjia.acg.controller.product;

import com.dangjia.acg.api.product.DjBasicsProductAPI;
import com.dangjia.acg.common.annotation.ApiMethod;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.modle.product.DjBasicsProduct;
import com.dangjia.acg.service.product.DjBasicsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 产品控制层
 * author: LJL
 * Date: 2019/9/00
 * Time: 13:56
 */
@RestController
public class DjBasicsProductController implements DjBasicsProductAPI {

    @Autowired
    private DjBasicsProductService djBasicsProductService;


    @Override
    @ApiMethod
    public ServerResponse queryProductData(HttpServletRequest request, String name) {
        return djBasicsProductService.queryProductData(name);

    }

    @Override
    public DjBasicsProduct queryProductDataByID(HttpServletRequest request, String id) {
        return djBasicsProductService.queryProductDataByID(request,id);
    }
    @Override
    @ApiMethod
    public ServerResponse queryProductLabels(HttpServletRequest request, String productId) {
        return djBasicsProductService.queryProductLabels(productId);
    }

    @Override
    @ApiMethod
    public ServerResponse addLabelsValue(HttpServletRequest request, String jsonStr) {
        return djBasicsProductService.addLabelsValue(jsonStr);
    }

}
