package com.dangjia.acg.dto.core;

import lombok.Data;

/**
 * @author Ruking.Cheng
 * @descrilbe 按钮
 * @email 495095492@qq.com
 * @tel 18075121944
 * @date on 2019/5/28 6:14 PM
 */

@Data
public class ButtonListBean {
    private String url;
    /**
     * 按钮类型：
     * 设计师：
     *          1001：去量房
     *          1002：上传平面图
     *          1003：修改平面图
     *          1004：上传施工图
     *          1005：修改施工图
     *          1006：上传设计图
     * 精算师：
     *          2001：确认装修信息
     *          2002：审核图纸
     *          2003：请等待业主选择
     *          2004：请等待设计师制作完毕
     *          2005：已结束
     *          2006：上传平面图
     *          2007：上传施工图
     * 大管家：
     *          3001：巡查工地
     *          3002：确认开工(待排期)
     *          3003：确认开工(已排期)
     *          3004：申请业主验收
     *          3005：查看拿钱明细
     *          3006：追加巡查
     *
     *          --工程进度-明细（头部按钮）
     *          3010：申请停工
     *          3011：奖罚
     *          3012：更换工匠
     *          3013：换人审核中
     *
     *          --工程进度-明细（底部按钮）
     *          3020：生成二维码
     *          3021：审核阶段完工
     *          3022：审核整体完工
     *          3023：提前进场
     * 工匠：
     *          4001：购买保险
     *          4002：查看拿钱明细
     *          4003：找大管家交底
     *          4004：今日开工
     *          4005：今日完工
     *          4006：申请整体完工
     *          4007：申请阶段完工
     * 业主：
     *          5001：需要修改设计
     *          5002：确认平面图
     *          5003：确认施工图
     *          5004：确认设计
     *          5005：申请额外修改设计
     * 维修：
     *          6001：上传勘查结果
     *
     *          6002：提前结束/已确认可开工
     *          6003：申请报销/申请验收
     * 体验：
     *          7001：上传验房结果
     * 督导：
     *          8001：奖罚
     *          8002：巡检
     */
    private int buttonType;//0:跳转URL，主按钮提示1：巡查工地2：申请业主验收；3:确认开工--主按钮提示 1:找大管家交底2:今日开工;3：今日完工;4阶段完工；5整体完工；6装修排期
    private String buttonTypeName;//主按钮提示 巡查工地;申请业主验收;确认开工--主按钮提示 1:找大管家交底2:今日开工;3：今日完工;4阶段完工；5整体完工；5整体完工；6装修排期
    private String buttonMsg;//按钮弹出描述，跨行使用"\n"
}
