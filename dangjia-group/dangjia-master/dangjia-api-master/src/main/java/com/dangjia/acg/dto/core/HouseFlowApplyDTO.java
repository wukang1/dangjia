package com.dangjia.acg.dto.core;

import lombok.Data;

import java.util.List;

/**
 * author: Ronalcheng
 * Date: 2018/11/26 0026
 * Time: 20:36
 */
@Data
public class HouseFlowApplyDTO {

    private String houseFlowApplyId;
    private int applyType;//0每日完工申请，1阶段完工申请，2整体完工申请, 3大管家申请直接调用业主对大管家审核
    private String headA;//工匠头像
    private String nameA;//工匠名字
    private String mobileA;//工匠电话
    private String workerTypeName;//工种名
    private List<String> imageList;//工地图片
    private String headB;//大管家头像
    private String nameB;//大管家名字
    private String mobileB;//大管家手机
    private String date;
}
