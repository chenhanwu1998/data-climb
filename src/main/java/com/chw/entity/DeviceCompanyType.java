package com.chw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DeviceCompanyType {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer proId;
    private String deviceType;
    private String cnName;
    private String name;
    private String enName;
    private String isFamous;
    private Integer proNum;
    private Integer wap_sequence;
    private Integer subcateId;
    private String wxCnt;
    private String starUserId;
    private Integer allProNum;
    private Integer saleLevel;
    private String logo;
    private Integer avNum;
    private Integer articleNum;
    private String picFileType;
    private Integer proPriceNum;
    private Integer reviewNum;
    private Integer bmsSaleLevel;
    private Integer sequence;
    private String trademark_pic;
    private String firstWord;
}
