package com.dangjia.acg.mapper.core;

import com.dangjia.acg.modle.product.BasicsGoodsCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * 
   * @类 名： BasicsGoodsCategoryDao
   * @功能描述： 类别dao
   * @作者信息： fzh
   * @创建时间： 2019-9-11
 */
@Repository
public interface IMasterBasicsGoodsCategoryMapper extends Mapper<BasicsGoodsCategory> {

    List<BasicsGoodsCategory> queryHouseWarehouseGoodsCategory(@Param("houseId") String houseId);

    //根据父id查询下属商品类型(分店铺还是城市)
    List<BasicsGoodsCategory> queryCategoryListByType(@Param("parentId") String parentId,@Param("sourceType") Integer sourceType,@Param("storefrontId") String storefrontId);

}
