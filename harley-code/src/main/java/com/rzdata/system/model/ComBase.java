package com.rzdata.system.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author hanj
 * @version 1.0
 * @description: TODO
 * @date 2021/12/27 19:26
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_com_base")
public class ComBase {
    private static final long serialVersionUID=1L;


    /**
     * 主键
     */
    @TableId(value = "id", type= IdType.ASSIGN_UUID)
    private String id;

    /**
     * 业务内容（json）
     */
    private String bizData;

    /**
     * 活动id
     */
    private String activeId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 企业类型（qy：企业；lsd：零售店）
     */
    private String enterpriseType;

    /**
     * 信用代码
     */
    private String enterpriseId;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 县（区）
     */
    private String county;

    /**
     * 生成经营业务类别(1:电子烟用烟碱（尼古丁）生产,2:电子烟烟液（烟油）生产,3:烟弹生产,4:烟具（含一次性、开放式电子烟）生产,5:国内电子烟品牌持有企业经营,6:国产品牌代理（分销）、外国电子烟产品进口（代理、分销）)
     */
    private String bizType;

    /**
     * 产品类别
     （1：境内生产并销售的电子烟产品。
     2： 进口代理的电子烟产品。
     3： 出口代加工的电子烟产品。）
     */
    private String productType;

    /**
     * 状态（zc：暂存；tj：提交, sc: 删除）
     */
    private String status;

    /**
     * 填报人（openId)
     */
    private String submitUserId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 实地核查结果确认
     */
    private String resultFlag;

    /**
     * 其他说明事项
     */
    private String resultMsg;

    /**
     * 企业状态(tbz: 填报中，tj: 已提交)
     */
    private String enterpriseStatus;

    /**
     * 产品状态(tbz: 填报中，tj: 已提交)
     */
    private String productStatus;
}
