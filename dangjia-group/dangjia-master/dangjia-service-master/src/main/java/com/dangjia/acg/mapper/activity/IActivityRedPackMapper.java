package com.dangjia.acg.mapper.activity;

import com.dangjia.acg.dto.activity.ActivityRedPackDTO;
import com.dangjia.acg.modle.activity.ActivityRedPack;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * author: qiyuxiang
 * Date: 2018/11/13 0031
 * Time: 17:01
 */
@Repository
public interface IActivityRedPackMapper extends Mapper<ActivityRedPack> {
    List<ActivityRedPackDTO> queryActivityRedPackRecords(@Param("sourceType") Integer sourceType,
                                                         @Param("status") String status,
                                                         @Param("storefrontId") String storefrontId,
                                                         @Param("cityId") String cityId);

    ActivityRedPackDTO getNewActivityRedPackDetail(@Param("redPackId") String redPackId);

}
