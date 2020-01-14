package com.dangjia.acg.mapper.engineer;

import com.dangjia.acg.dto.engineer.DjMaintenanceRecordDTO;
import com.dangjia.acg.modle.engineer.DjMaintenanceRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * author: wk
 * Date: 13/12/2019
 * Time: 上午 9:59
 */
@Repository
public interface DjMaintenanceRecordMapper extends Mapper<DjMaintenanceRecord> {

    List<DjMaintenanceRecordDTO> queryDjMaintenanceRecordList(@Param("searchKey") String searchKey,
                                                              @Param("state") Integer state);

    DjMaintenanceRecordDTO queryDjMaintenanceRecordDetail(@Param("id") String id);

    //判断是否有正在处理中的质保
    List<DjMaintenanceRecord> selectMaintenanceRecoredByHouseId(@Param("houseId") String houseId,@Param("workerTypeSafeOrderId") String workerTypeSafeOrderId);

}
