package com.chw.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DeviceBasicInfo {

    @TableId(value = "id")
    private Integer id;

    private String deviceType;
    /**
     * 对应CompanyType的id
     */
    private Integer proId;

    private String detailUrl;

    private String remark;

    private String name;

    private String imgUrl;

    private Double price;

    private Double score;

    private Long heat;

    private String commentUrl;


}
