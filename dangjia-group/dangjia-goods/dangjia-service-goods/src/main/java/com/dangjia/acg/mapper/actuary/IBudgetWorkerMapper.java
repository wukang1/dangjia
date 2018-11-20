package com.dangjia.acg.mapper.actuary;

import com.dangjia.acg.modle.actuary.BudgetWorker;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
   * @类 名： BudgetWorkerDao.java
   * @功能描述：  
   * @作者信息： hb
   * @创建时间： 2018-9-15下午3:15:10
 */
@Repository
public interface IBudgetWorkerMapper extends Mapper<BudgetWorker> {
	/**
	 * 未删除人工精算
	 */
	List<BudgetWorker> getBudgetWorkerList(@Param("houseId")String houseId, @Param("workerTypeId")String workerTypeId);

	/**查询所有精算*/
	List<Map<String,Object>> getBudgetWorker();
	/**根据风格查询所有精算*/
	List<Map<String,Object>> getAllbudgetTemplates(String template_id);
	/**根据houseId和wokerTypeId查询房子人工精算*/
	List<Map<String,Object>> getBudgetWorkerById(@Param("houseId")String houseId, @Param("workerTypeId")String workerTypeId);
	/**根据拿到的Id删除精算*/
	void deleteById(String budgetWorkerId);
	/**根据拿到的Id删除精算*/
	void deleteBytemplateId(String template_id);
	/**根据houseId删除材料精算*/
	void deleteByhouseId(@Param("houseId")String houseId, @Param("workerTypeId")String workerTypeId);
	/**根据houseId和wokerTypeId查询房子人工精算总价*/
	Map<String,Object> getWorkerTotalPrice(@Param("houseId")String houseId, @Param("workerTypeId")String workerTypeId);
}
