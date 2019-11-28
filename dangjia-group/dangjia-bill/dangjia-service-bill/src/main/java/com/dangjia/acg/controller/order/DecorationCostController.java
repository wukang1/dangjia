package com.dangjia.acg.controller.order;


import com.dangjia.acg.api.order.DecorationCostAPI;
import com.dangjia.acg.common.annotation.ApiMethod;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.service.order.DecorationCostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DecorationCostController implements DecorationCostAPI {
    protected static final Logger logger = LoggerFactory.getLogger(DecorationCostController.class);

    @Autowired
    private DecorationCostService decorationCostService;
    /**
     * 查询当前花费信息
     * @param userToken
     * @param cityId
     * @param houseId
     * @param labelValId
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse searchDecorationCostList(PageDTO pageDTO,String userToken, String cityId, String houseId, String labelValId) {
        return decorationCostService.searchDecorationCostList(pageDTO,userToken,cityId,houseId,labelValId);
    }

    /**
     * 查询当前花费列表商品信息
     */
    @Override
    @ApiMethod
    public ServerResponse searchDecorationCostProductList(String cityId,String houseId,String labelValId,String categoryId){
        return decorationCostService.searchDecorationCostProductList(cityId,houseId,labelValId,categoryId);
    }

    /**
     * 查询分类标签汇总信息
     * @param userToken
     * @param cityId
     * @param houseId
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse searchDecorationCategoryLabelList(String userToken,String cityId,String houseId){
        return decorationCostService.searchDecorationCategoryLabelList(userToken,cityId,houseId);
    }
    /**
     * 录入自购商品价格信息
     * @param userToken 用户TOKEN
     * @param cityId  城市ID
     * @param actuaryBudgetId 精算设置ID
     * @return
     */
    @Override
    @ApiMethod
    public ServerResponse editPurchasePrice(String userToken, String cityId,String actuaryBudgetId,Double shopCount,Double totalPrice,Integer housekeeperAcceptance){
        return decorationCostService.editPurchasePrice(userToken,cityId,actuaryBudgetId,shopCount,totalPrice,housekeeperAcceptance);
    }
}
