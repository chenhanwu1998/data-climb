package com.chw.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DeviceParam {

    @TableId(value = "device_id")
    private Integer deviceId;
    private String publishTime;
    private String phoneType;
    private String systemKernel;
    private String rom;
    private String ram;
    private String price;
    private String cpuFreq;
    private String procetedTime;
    private String cameraNum;
    private String marketTime;
    private String cpuCoreNum;

    private String basicParam;
    private String hardware;
    private String screen;
    private String battery;
    private String network;
    private String appearance;
    private String camera;
    private String service;
    private String protectedInfo;

}
