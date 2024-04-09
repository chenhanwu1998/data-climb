package com.chw.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ComputerDetail {

    @TableId(value = "device_id")
    private Integer deviceId;
    private String cpu;
    private String cpuBest;
    private Double cpuLevel;
    private String cpuDesc;
    private String ram;
    private String ramBest;
    private Double ramLevel;
    private String ramDesc;
    private String screen;
    private String screenBest;
    private Double screenLevel;
    private String screenDesc;
    private String resRatio;
    private String resRatioBest;
    private Double resRatioLevel;
    private String resRatioDesc;
    private String weight;
    private Double weightLevel;
    private String weightBest;
    private String weightDesc;
}
