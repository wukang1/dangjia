package com.dangjia.acg.service.home;

import com.dangjia.acg.common.constants.SysConfig;
import com.dangjia.acg.common.exception.ServerCode;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.common.util.CommonUtil;
import com.dangjia.acg.dao.ConfigUtil;
import com.dangjia.acg.dto.matter.RenovationManualDTO;
import com.dangjia.acg.mapper.core.IHouseFlowApplyMapper;
import com.dangjia.acg.mapper.core.IHouseFlowMapper;
import com.dangjia.acg.mapper.core.IWorkerTypeMapper;
import com.dangjia.acg.mapper.house.IHouseMapper;
import com.dangjia.acg.mapper.house.IWebsiteVisitMapper;
import com.dangjia.acg.mapper.matter.IRenovationManualMapper;
import com.dangjia.acg.mapper.matter.IRenovationStageMapper;
import com.dangjia.acg.mapper.member.IMemberMapper;
import com.dangjia.acg.modle.core.HouseFlow;
import com.dangjia.acg.modle.core.HouseFlowApply;
import com.dangjia.acg.modle.core.WorkerType;
import com.dangjia.acg.modle.house.House;
import com.dangjia.acg.modle.matter.RenovationStage;
import com.dangjia.acg.modle.member.Member;
import com.dangjia.acg.service.core.CraftsmanConstructionService;
import com.dangjia.acg.service.house.MyHouseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ruking.Cheng
 * @descrilbe 首页配置实现
 * @email 495095492@qq.com
 * @tel 18075121944
 * @date on 2019/6/13 3:22 PM
 */
@Service
public class HomeModularService {
    @Autowired
    private IHouseMapper iHouseMapper;
    @Autowired
    private IMemberMapper iMemberMapper;
    @Autowired
    private IWorkerTypeMapper iWorkerTypeMapper;
    @Autowired
    private IHouseFlowApplyMapper iHouseFlowApplyMapper;
    @Autowired
    private IRenovationManualMapper renovationManualMapper;
    @Autowired
    private IRenovationStageMapper renovationStageMapper;
    @Autowired
    private ConfigUtil configUtil;
    @Autowired
    private CraftsmanConstructionService constructionService;
    @Autowired
    private IHouseFlowMapper houseFlowMapper;
    @Autowired
    private MyHouseService myHouseService;

    public ServerResponse getBroadcastList() {
        PageHelper.startPage(1, 20);
        Example example = new Example(HouseFlowApply.class);
        //过滤掉提前结束的房子
        example.createCriteria()
                .andNotEqualTo(HouseFlowApply.APPLY_TYPE, 8)
                .andNotEqualTo(HouseFlowApply.APPLY_TYPE, 3)
                .andNotEqualTo(HouseFlowApply.APPLY_TYPE, 9);
        example.orderBy(HouseFlowApply.CREATE_DATE).desc();
        List<HouseFlowApply> houseFlowApplies = iHouseFlowApplyMapper.selectByExample(example);
        if (houseFlowApplies.size() <= 0) {
            return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
        }
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (HouseFlowApply houseFlowApply : houseFlowApplies) {
            Map<String, Object> map = new HashMap<>();
            StringBuilder describe = new StringBuilder();
            House house = iHouseMapper.selectByPrimaryKey(houseFlowApply.getHouseId());
            if (house == null) {
                continue;
            }
            describe.append(house.getNoNumberHouseName());
            Member member = iMemberMapper.selectByPrimaryKey(houseFlowApply.getWorkerId());
            if (member != null) {
                WorkerType workerType = iWorkerTypeMapper.selectByPrimaryKey(member.getWorkerTypeId());
                if (workerType != null) {
                    describe.append(" ");
                    describe.append(workerType.getName());
                }
            }
            switch (houseFlowApply.getApplyType()) {
                case 0:
                    describe.append("今日已完工");
                    break;
                case 1:
                    describe.append("今日阶段完工");
                    break;
                case 2:
                    describe.append("今日整体完工");
                    break;
                case 3:
                    describe.append("今日已停工");
                    break;
                case 4:
                    describe.append("已开工");
                    break;
                default:
                    describe.append("今日已巡查");
                    break;
            }
            map.put("describe", describe.toString());
            map.put("houseId", houseFlowApply.getHouseId());
            listMap.add(map);
        }
        return ServerResponse.createBySuccess("查询成功", listMap);
    }

    public ServerResponse getStrategyList(String userToken, PageDTO pageDTO) {
        List<String> workerTypeIds = new ArrayList<>();
        if (!CommonUtil.isEmpty(userToken)) {
            Object object = constructionService.getMember(userToken);
            if (object instanceof Member) {
                Member member = (Member) object;
                object = myHouseService.getHouse(member.getId(), null);
                if (object instanceof House) {
                    House house = (House) object;
                    if (house.getDesignerOk() != 3) {//设计阶段
                        setWorkerTypeIds("1", workerTypeIds);
                    } else if (house.getBudgetOk() != 3) {//精算阶段
                        setWorkerTypeIds("2", workerTypeIds);
                    } else {
                        Example example = new Example(HouseFlow.class);
                        example.createCriteria()
                                .andEqualTo(HouseFlow.WORK_TYPE, 4)
                                .andNotEqualTo(HouseFlow.WORK_STETA, 2)
                                .andEqualTo(HouseFlow.HOUSE_ID, house.getId());
                        example.orderBy(HouseFlow.SORT).asc();
                        List<HouseFlow> houseFlows = houseFlowMapper.selectByExample(example);
                        if (houseFlows.size() > 0) {
                            for (int i = houseFlows.size() - 1; i >= 0; i--) {
                                if (house.getVisitState() == 1) {//施工阶段
                                    HouseFlow houseFlow = houseFlows.get(i);
                                    setWorkerTypeIds(houseFlow.getWorkerTypeId(), workerTypeIds);
                                }
                            }
                        }
                    }
                }
            }
        }
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<RenovationManualDTO> rmList = renovationManualMapper.getStrategyList(workerTypeIds.size() <= 0 ? null
                : workerTypeIds.toArray(new String[workerTypeIds.size()]));
        if (rmList.size() <= 0) {
            return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
        }
        PageInfo pageResult = new PageInfo(rmList);
        String imageAddress = configUtil.getValue(SysConfig.DANGJIA_IMAGE_LOCAL, String.class);
        for (RenovationManualDTO renovationManual : rmList) {
            renovationManual.setImageUrl(CommonUtil.isEmpty(renovationManual.getImage()) ? null : (imageAddress + renovationManual.getImage()));
        }
        pageResult.setList(rmList);
        return ServerResponse.createBySuccess("获取所有装修指南成功", pageResult);
    }

    private void setWorkerTypeIds(String workerTypeId, List<String> workerTypeIds) {
        Example example = new Example(RenovationStage.class);
        example.createCriteria()
                .andEqualTo(RenovationStage.WORKER_TYPE_ID, workerTypeId)
                .andEqualTo(RenovationStage.DATA_STATUS, 0);
        List<RenovationStage> rmList = renovationStageMapper.selectByExample(example);
        if (rmList.size() > 0) {
            for (RenovationStage renovationStage : rmList) {
                workerTypeIds.add(renovationStage.getId());
            }
        }
    }
}
