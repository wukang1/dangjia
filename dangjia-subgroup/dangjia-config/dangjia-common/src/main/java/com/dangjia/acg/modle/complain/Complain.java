package com.dangjia.acg.modle.complain;

import com.dangjia.acg.common.annotation.Desc;
import com.dangjia.acg.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "dj_complain")
@FieldNameConstants(prefix = "")
@ApiModel(description = "当家申诉处理")
public class Complain extends BaseEntity {

    @Column(name = "member_id")
    @Desc(value = "对象ID")
    @ApiModelProperty("对象ID")
    private String memberId;

    @Column(name = "complain_type")
    @Desc(value = "申述类型 1: 被处罚申诉.2：要求整改.3：要求换人.4:部分收货申诉.5:提前结束装修.6业主要求换人.7:业主申诉部分退货.8工匠申请部分退货.9工匠报销.10工匠申维保定责")
    @ApiModelProperty("申述类型 1: 被处罚申诉.2：要求整改.3：要求换人.4:部分收货申诉.5:提前结束装修.6业主要求换人.7:业主申诉部分退货.8工匠申请部分退货.9工匠报销.10工匠申维保定责")
    private Integer complainType;

    @Column(name = "user_id")
    @Desc(value = "发起人ID")
    @ApiModelProperty("发起人ID")
    private String userId;

    @Column(name = "status")
    @Desc(value = "处理状态.0:待处理。1.驳回。2.接受。")
    @ApiModelProperty("处理状态.0:待处理。1.驳回。2.接受。")
    private Integer status;

    @Column(name = "description")
    @Desc(value = "处理描述")
    @ApiModelProperty("处理描述")
    private String description;

    @Column(name = "business_id")
    @Desc(value = "对应业务ID  " +
            "complain_type==1:对应处罚的rewardPunishRecordId, " +
            "complain_type==2:对应工匠memberId," +
            "complain_type==3:对应工匠memberId," +
            "complain_type==4:发货单splitDeliverId,")
    @ApiModelProperty("对应业务ID")
    private String businessId;

    @Column(name = "house_id")
    @Desc(value = "对应房子ID")
    @ApiModelProperty("对应房子ID")
    private String houseId;


    @Column(name = "files")
    @Desc(value = "附件")
    @ApiModelProperty("附件")
    private String files;

    @Column(name = "content")
    @Desc(value = "对象名称")
    @ApiModelProperty("对象名称")
    private String content;

    @Column(name = "user_name")
    @Desc(value = "发起人名称")
    @ApiModelProperty("发起人名称")
    private String userName;

    @Column(name = "operate_id")
    @Desc(value = "操作人ID")
    @ApiModelProperty("操作人ID")
    private String operateId;

    @Column(name = "operate_name")
    @Desc(value = "操作人姓名")
    @ApiModelProperty("操作人姓名")
    private String operateName;


    @Column(name = "user_nick_name")
    @Desc(value = "发起人昵称")
    @ApiModelProperty("发起人昵称")
    private String userNickName;


    @Column(name = "user_mobile")
    @Desc(value = "发起人电话")
    @ApiModelProperty("发起人电话")
    private String userMobile;


    @Column(name = "change_reason")
    @Desc(value = "更换原因")
    @ApiModelProperty("更换原因")
    private String changeReason;

    @Column(name = "reject_reason")
    @Desc(value = " 驳回原因")
    @ApiModelProperty(" 驳回原因")
    private String rejectReason;

    @Column(name = "image")
    @Desc(value = "图片")
    @ApiModelProperty("图片")
    private String image;

    @Column(name = "handle_date")
    @Desc(value = "平台处理时间")
    @ApiModelProperty("平台处理时间")
    private Date handleDate;

    @Column(name = "apply_money")
    @Desc(value = "申请报销金钱")
    @ApiModelProperty("申请报销金额")
    private Double applyMoney;

    @Column(name = "actual_money")
    @Desc(value = "实际报销金额")
    @ApiModelProperty("实际报销金额")
    private Double actualMoney;

    @Column(name = "application_status")
    @Desc(value = "申请身份：1工匠，2业主，3店铺，4供应商")
    @ApiModelProperty("申请身份：1工匠，2业主，3店铺，4供应商")
    private Integer applicationStatus;


}