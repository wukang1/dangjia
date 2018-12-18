package com.dangjia.acg.service.basics;

import com.alibaba.fastjson.JSONObject;
import com.dangjia.acg.api.data.WorkerTypeAPI;
import com.dangjia.acg.common.constants.SysConfig;
import com.dangjia.acg.common.enums.EventStatus;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.dao.ConfigUtil;
import com.dangjia.acg.dto.basics.TechnologyDTO;
import com.dangjia.acg.dto.basics.WorkerGoodsDTO;
import com.dangjia.acg.mapper.basics.ITechnologyMapper;
import com.dangjia.acg.mapper.basics.IWorkerGoodsMapper;
import com.dangjia.acg.modle.basics.Technology;
import com.dangjia.acg.modle.basics.WorkerGoods;
import com.dangjia.acg.modle.basics.WorkerTechnology;
import com.dangjia.acg.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ruking.Cheng
 * @descrilbe 工价商品Service实现
 * @email 495095492@qq.com
 * @tel 18075121944
 * @date on 2018/9/12 上午10:59
 */
@Service
public class WorkerGoodsService {

    @Autowired
    private IWorkerGoodsMapper iWorkerGoodsMapper;
    @Autowired
    private ITechnologyMapper iTechnologyMapper;
    @Autowired
    private ConfigUtil configUtil;
    @Autowired
    private WorkerTypeAPI workerTypeAPI;


    public ServerResponse<PageInfo> getWorkerGoodses(Integer pageNum, Integer pageSize, String workerTypeId, String searchKey, String showGoods) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<WorkerGoods> productList = iWorkerGoodsMapper.selectList(StringUtils.isBlank(workerTypeId) ? null : workerTypeId,
                StringUtils.isBlank(searchKey) ? null : searchKey, StringUtils.isBlank(showGoods) ? null : showGoods);
        if (productList == null || productList.size() <= 0) {
            return ServerResponse.createByErrorCodeMessage(EventStatus.NO_DATA.getCode(), "暂无工价商品");
        }
        List<WorkerGoodsDTO> workerGoodsResults = new ArrayList<WorkerGoodsDTO>();
        for (WorkerGoods workerGoods : productList) {
            WorkerGoodsDTO workerGoodsResult = assembleWorkerGoodsResult(workerGoods);
            workerGoodsResults.add(workerGoodsResult);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(workerGoodsResults);
        return ServerResponse.createBySuccess("获取工价商品列表成功", pageResult);
    }

    private WorkerGoodsDTO assembleWorkerGoodsResult(WorkerGoods workerGoods) {
        try {
            String address = configUtil.getValue(SysConfig.PUBLIC_DANGJIA_ADDRESS, String.class);
            WorkerGoodsDTO workerGoodsResult = new WorkerGoodsDTO();
            workerGoodsResult.setId(workerGoods.getId());
            workerGoodsResult.setName(workerGoods.getName());
            workerGoodsResult.setWorkerGoodsSn(workerGoods.getWorkerGoodsSn());
            String[] imgArr = workerGoods.getImage().split(",");
            String imgStr = "";
            String imgUrlStr = "";
            for (int i = 0; i < imgArr.length; i++) {
                if (i == imgArr.length - 1) {
                    imgStr += address + imgArr[i];
                    imgUrlStr += imgArr[i];
                } else {
                    imgStr += address + imgArr[i] + ",";
                    imgUrlStr += imgArr[i] + ",";
                }
            }
            String[] imgArr2 = workerGoods.getWorkerDec().split(",");
            String imgStr2 = "";
            String imgUrlStr2 = "";
            for (int i = 0; i < imgArr2.length; i++) {
                if (i == imgArr2.length - 1) {
                    imgStr2 += address + imgArr2[i];
                    imgUrlStr2 += imgArr2[i];
                } else {
                    imgStr2 += address + imgArr2[i] + ",";
                    imgUrlStr2 += imgArr2[i] + ",";
                }
            }
            workerGoodsResult.setImage(imgStr);
            workerGoodsResult.setImageUrl(imgUrlStr);
            workerGoodsResult.setWorkerDec(imgStr2);
            workerGoodsResult.setWorkerDecUrl(imgUrlStr2);
            workerGoodsResult.setUnitId(workerGoods.getUnitId());
            workerGoodsResult.setUnitName(workerGoods.getUnitName());

            String workerTypeName = "";
            ServerResponse serverResponse = workerTypeAPI.getNameByWorkerTypeId(workerGoods.getWorkerTypeId());
            if (serverResponse.isSuccess()) {
                workerTypeName = (String) serverResponse.getResultObj();
            }
            workerGoodsResult.setWorkerTypeName(workerTypeName);
            workerGoodsResult.setPrice(workerGoods.getPrice());
            workerGoodsResult.setSales(workerGoods.getSales());
            workerGoodsResult.setWorkExplain(workerGoods.getWorkExplain());
            workerGoodsResult.setWorkerStandard(workerGoods.getWorkerStandard());
            workerGoodsResult.setWorkerTypeId(workerGoods.getWorkerTypeId());
            workerGoodsResult.setWorkerTypeName(workerTypeName);
            workerGoodsResult.setShowGoods(workerGoods.getShowGoods());
            //将工艺列表返回
            List<TechnologyDTO> technologies = new ArrayList<>();
            List<WorkerTechnology> wokerTechnologies = iTechnologyMapper.queryWokerTechnologyByWgId(workerGoods.getId());
            if (wokerTechnologies != null) {
                for (WorkerTechnology workerTechnology : wokerTechnologies) {
                    Technology technology = iTechnologyMapper.queryById(workerTechnology.getTechnologyId());
                    if (technology != null) {
                        TechnologyDTO technologyResult = new TechnologyDTO();
                        technologyResult.setId(technology.getId());
                        technologyResult.setName(technology.getName());
                        technologyResult.setWorkerTypeId(technology.getWorkerTypeId());
                        technologyResult.setContent(technology.getContent());
                        String imgStr3 = "";
                        String imgUrlStr3 = "";
                        if (technology.getImage() != null) {
                            String[] imgArr3 = technology.getImage().split(",");
                            for (int i = 0; i < imgArr3.length; i++) {
                                if (i == imgArr3.length - 1) {
                                    imgStr3 += address + imgArr3[i];
                                    imgUrlStr3 += imgArr3[i];
                                } else {
                                    imgStr3 += address + imgArr3[i] + ",";
                                    imgUrlStr3 += imgArr3[i] + ",";
                                }
                            }
                        }
                        technologyResult.setImage(imgStr3);
                        technologyResult.setImageUrl(imgUrlStr3);
                        technologyResult.setCreateDate(DateUtils.timedate(String.valueOf(technology.getCreateDate().getTime())));
                        technologyResult.setModifyDate(DateUtils.timedate(String.valueOf(technology.getModifyDate().getTime())));
                        technologies.add(technologyResult);
                    }
                }
            }
            workerGoodsResult.setTechnologies(technologies);
            workerGoodsResult.setCreateDate(DateUtils.timedate(String.valueOf(workerGoods.getCreateDate().getTime())));
            workerGoodsResult.setModifyDate(DateUtils.timedate(String.valueOf(workerGoods.getModifyDate().getTime())));
            return workerGoodsResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ServerResponse<String> setWorkerGoods(WorkerGoods workerGoods, String technologyIds) {

        List<WorkerGoods> workerGoodsList = iWorkerGoodsMapper.selectByName(workerGoods.getName(), workerGoods.getWorkerTypeId());
        if (workerGoodsList.size() > 0) {
            return ServerResponse.createBySuccessMessage("商品名称不能重复");
        }

        List<WorkerGoods> workerGoodsList1 = iWorkerGoodsMapper.selectByWorkerGoodsSn(workerGoods.getWorkerGoodsSn(), workerGoods.getWorkerTypeId());
        if (workerGoodsList1.size() > 0) {
            return ServerResponse.createBySuccessMessage("商品编号不能重复");
        }

        if (workerGoods != null) {
            if (workerGoods.getImage() != null && !"".equals(workerGoods.getImage())) {
                String[] imgArr = workerGoods.getImage().split(",");
                String imgStr = "";
                for (int i = 0; i < imgArr.length; i++) {
                    String img = imgArr[i];
                    if (i == imgArr.length - 1) {
                        imgStr += img;
                    } else {
                        imgStr += img + ",";
                    }
                }
                workerGoods.setImage(imgStr);
            }
            if (workerGoods.getWorkerDec() != null && !"".equals(workerGoods.getWorkerDec())) {
                String[] imgArr2 = workerGoods.getWorkerDec().split(",");
                String imgStr2 = "";
                for (int i = 0; i < imgArr2.length; i++) {
                    String img = imgArr2[i];
                    if (i == imgArr2.length - 1) {
                        imgStr2 += img;
                    } else {
                        imgStr2 += img + ",";
                    }
                }
                workerGoods.setWorkerDec(imgStr2);//商品介绍图片
            }
            WorkerGoods workerG = iWorkerGoodsMapper.selectByPrimaryKey(workerGoods.getId());
            if (StringUtils.isNotBlank(workerGoods.getId()) && workerG != null) {
                workerGoods.setModifyDate(new Date());
                int rowCount = iWorkerGoodsMapper.updateByPrimaryKeySelective(workerGoods);
                if (rowCount > 0) {
                    ServerResponse<String> serverResponse = setTechnologyId(workerGoods.getId(), technologyIds);
                    if (serverResponse.isSuccess()) {
                        return ServerResponse.createBySuccessMessage("更新工价商品成功");
                    }
                }
                return ServerResponse.createByErrorMessage("更新工价商品失败");
            } else {
                workerGoods.setCreateDate(new Date());
                workerGoods.setModifyDate(new Date());
                int rowCount = iWorkerGoodsMapper.insert(workerGoods);
                if (rowCount > 0) {
                    ServerResponse<String> serverResponse = setTechnologyId(workerGoods.getId(), technologyIds);
                    if (serverResponse.isSuccess()) {
                        return ServerResponse.createBySuccessMessage("新增工价商品成功");
                    }
                }
                return ServerResponse.createByErrorMessage("新增工价商品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新工价商品参数不正确");
    }

    private ServerResponse<String> setTechnologyId(String workerGoodsId, String technologyIds) {
        if (!StringUtils.isNotBlank(workerGoodsId)) {
            return ServerResponse.createByErrorMessage("请选择商品");
        }
        if (!StringUtils.isNotBlank(technologyIds)) {
            return ServerResponse.createByErrorMessage("请选择工艺");
        }
        try {
            iTechnologyMapper.deleteWokerTechnologyByWgId(workerGoodsId);
            String[] ids = technologyIds.split(",");
            for (String id : ids) {
                if (StringUtils.isNotBlank(id)) {
                    WorkerTechnology wt = new WorkerTechnology();
                    wt.setWorkerGoodsId(workerGoodsId);
                    wt.setTechnologyId(id);
                    wt.setCreateDate(new Date());
                    wt.setModifyDate(new Date());
                    iTechnologyMapper.insertWokerTechnology(wt);// 需要将工艺替换
                }
            }
            return ServerResponse.createBySuccessMessage("修改商品工艺成功");
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("修改商品工艺时数据库发生错误");
        }
    }

    /**
     * 每工种未删除 或 已支付工钱
     *
     * @param houseId
     * @param houseFlowId
     * @return
     */
    public ServerResponse getWorkertoCheck(String houseId, String houseFlowId) {
        try {
            Double totalPrice = iWorkerGoodsMapper.getWorkertoCheck(houseId, houseFlowId);
            JSONObject object = new JSONObject();
            object.put("totalPrice", totalPrice);
            return ServerResponse.createBySuccess("查询未删除或已支付的工钱成功", object);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询未删除或已支付的工钱失败");

        }

    }

    /**
     * 从精算表查工种已支付工钱
     *
     * @param houseId
     * @param houseFlowId
     * @return
     */
    public ServerResponse getPayedWorker(String houseId, String houseFlowId) {
        try {
            Double totalPrice = iWorkerGoodsMapper.getPayedWorker(houseId, houseFlowId);
            JSONObject object = new JSONObject();
            object.put("totalPrice", totalPrice);
            return ServerResponse.createBySuccess("查询未删除或已支付的工钱成功", object);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询未删除或已支付的工钱失败");

        }
    }

    /**
     * 删除人工商品
     *
     * @param id
     * @return
     */
    public ServerResponse deleteWorkerGoods(String id) {
        try {
            if (true) {
                return ServerResponse.createByErrorMessage("不能执行删除操作");
            }

            int i = iWorkerGoodsMapper.deleteByPrimaryKey(id);
            if (i != 0) {
                return ServerResponse.createBySuccessMessage("删除成功");
            } else {
                return ServerResponse.createByErrorMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("删除失败");

        }

    }

}
