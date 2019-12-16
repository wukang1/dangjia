package com.dangjia.acg.mapper.order;


import com.dangjia.acg.dto.refund.RefundRepairOrderMaterialDTO;
import com.dangjia.acg.modle.repair.MendWorker;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface IBillMendWorkerMapper extends Mapper<MendWorker>{


    List<RefundRepairOrderMaterialDTO> queryBillMendOrderId(@Param("mendOrderId") String mendOrderId);
}