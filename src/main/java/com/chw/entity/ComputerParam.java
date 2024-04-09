package com.chw.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ComputerParam {

    @TableId(value = "device_id")
    private Integer deviceId;
    private double price;
    private String procetedTime;
    private String diskCapacity;
    private String cpuSerial;
    private String cpuFreq;
    private String cpuType;
    private String coreSize;
    private String threeCache;
    private String rom;
    private String largestRom;
    private String weight;
    private String productLoc;
    private String osSystem;
    private String productType;
    private String nvidiaType;
    private String nvidiaCore;
    private String nvidiaRomType;
    private String nvidiaRom;

    private String other;
    private String appendix;
    private String mediaDevice;
    private String cpu;
    private String screen;
    private String storage;
    private String battery;
    private String network;
    private String appearance;
    private String basicParam;
    private String nvidia;
    private String ioInterface;
    private String protectedInfo;
}
