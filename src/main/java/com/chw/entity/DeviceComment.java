package com.chw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DeviceComment {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer deviceId;
    private String username;
    private String createDate;
    private String article;
}
