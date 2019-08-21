package com.dangjia.acg.service.sale.royalty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dangjia.acg.common.exception.ServerCode;
import com.dangjia.acg.common.model.BaseEntity;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.mapper.sale.DjRoyaltyMatchMapper;
import com.dangjia.acg.mapper.sale.RoyaltyMapper;
import com.dangjia.acg.mapper.sale.SurfaceMapper;
import com.dangjia.acg.modle.sale.royalty.DjRoyaltyDetailsSurface;
import com.dangjia.acg.modle.sale.royalty.DjRoyaltyMatch;
import com.dangjia.acg.modle.sale.royalty.DjRoyaltySurface;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * 提成配置模块
 * Created with IntelliJ IDEA.
 * author: ljl
 * Date: 2019/7/26
 * Time: 16:16
 */
@Service
public class RoyaltyService {
    @Autowired
    private RoyaltyMapper royaltyMapper;

    @Autowired
    private SurfaceMapper surfaceMapper;
    @Autowired
    private DjRoyaltyMatchMapper djRoyaltyMatchMapper;

    /**
     * 查询提成列表
     *
     * @return
     */
    public ServerResponse queryRoyaltySurface(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        List<BaseEntity> baseEntityList = royaltyMapper.queryRoyaltySurface();
        if (baseEntityList.size() <= 0) {
            return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
        }
        return ServerResponse.createBySuccess("查询提成列表", new PageInfo(baseEntityList));
    }

    /**
     * 新增提成信息
     *
     * @param lists
     * @return
     */
    public ServerResponse addRoyaltyData(String lists) {
        DjRoyaltySurface djRoyaltySurface = new DjRoyaltySurface();
        JSONArray list = JSON.parseArray(lists);
        //插入提成配置总表
        if (surfaceMapper.insert(djRoyaltySurface) > 0) {
            //循环插入提成配置详情表
            DjRoyaltyDetailsSurface djr = new DjRoyaltyDetailsSurface();
            djr.setVillageId(djRoyaltySurface.getId());
            for (int i = 0; i < list.size(); i++) {
                djr = new DjRoyaltyDetailsSurface();
                djr.setVillageId(djRoyaltySurface.getId());
                djr.setCreateDate(new Date());
                JSONObject JS = list.getJSONObject(i);
                djr.setStartSingle(JS.getInteger("startSingle"));
                djr.setOverSingle(JS.getInteger("overSingle"));
                djr.setRoyalty(JS.getInteger("royalty"));
                royaltyMapper.insert(djr);
            }
            return ServerResponse.createBySuccessMessage("提交成功");
        }
        return ServerResponse.createBySuccessMessage("提交失败");
    }

    /**
     * 查询提成详情
     *
     * @param id
     * @return
     */
    public ServerResponse queryRoyaltyData(String id) {
        Example example = new Example(DjRoyaltyDetailsSurface.class);
        example.createCriteria().andEqualTo(DjRoyaltyDetailsSurface.VILLAGE_ID, id)
                .andEqualTo(DjRoyaltyDetailsSurface.DATA_STATUS, 0);
        example.orderBy(DjRoyaltyDetailsSurface.CREATE_DATE).desc();
        List<DjRoyaltyDetailsSurface> djRoyaltyDetailsSurfaces = royaltyMapper.selectByExample(example);
        if (djRoyaltyDetailsSurfaces.size() <= 0) {
            return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
        }
        return ServerResponse.createBySuccess("查询提成列表", djRoyaltyDetailsSurfaces);
    }


    /**
     * 房子竣工拿提成
     * @param houseId
     */
    public void endRoyalty(String houseId){
        Example example=new Example(DjRoyaltyMatch.class);
        example.createCriteria().andEqualTo(DjRoyaltyMatch.HOUSE_ID,houseId)
                .andNotEqualTo(DjRoyaltyMatch.ORDER_STATUS,1);
        List<DjRoyaltyMatch> djRoyaltyMatches = djRoyaltyMatchMapper.selectByExample(example);
        for (DjRoyaltyMatch djRoyaltyMatch : djRoyaltyMatches) {
            DjRoyaltyMatch djRoyaltyMatch1=new DjRoyaltyMatch();
            djRoyaltyMatch1.setDataStatus(0);
            djRoyaltyMatch1.setOrderStatus(1);
            djRoyaltyMatch1.setUserId(djRoyaltyMatch.getUserId());
            djRoyaltyMatch1.setHouseId(djRoyaltyMatch.getHouseId());
            djRoyaltyMatch1.setMonthRoyalty((int) (djRoyaltyMatch.getBranchRoyalty()*0.25));
            djRoyaltyMatch1.setMeterRoyalty((int) (djRoyaltyMatch.getBranchRoyalty()*0.25)+djRoyaltyMatch.getMeterRoyalty());
            djRoyaltyMatch1.setArrRoyalty(djRoyaltyMatch1.getArrRoyalty());
            djRoyaltyMatchMapper.insert(djRoyaltyMatch1);
        }
    }


}
