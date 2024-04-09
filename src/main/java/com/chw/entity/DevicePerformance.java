package com.chw.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DevicePerformance {

    @TableId(value = "device_id")
    private Integer deviceId;
    private double endurance;
    private double costPerf;
    private double photo;
    private double performance;
    private String goodWord;
    private double totalScore;

}
