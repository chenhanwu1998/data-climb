package com.chw.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DeviceDetail {

    @TableId(value = "device_id")
    private Integer deviceId;
    private String cpu;
    private String cpuBest;
    private Double cpuLevel;
    private String cpuDesc;
    private String frontPix;
    private String frontPixBest;
    private Double frontPixLevel;
    private String frontPixDesc;
    private String backPix;
    private Double backPixLevel;
    private String backPixDesc;
    private String backPixBest;
    private String ram;
    private String ramBest;
    private Double ramLevel;
    private String ramDesc;
    private String battery;
    private String batteryBest;
    private Double batteryLevel;
    private String batteryDesc;
    private String screen;
    private String screenBest;
    private Double screenLevel;
    private String screenDesc;
    private String resRatio;
    private String resRatioBest;
    private Double resRatioLevel;
    private String resRatioDesc;
}
