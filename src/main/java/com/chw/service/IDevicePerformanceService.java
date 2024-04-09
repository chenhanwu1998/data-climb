package com.chw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.entity.DevicePerformance;

public interface IDevicePerformanceService extends IService<DevicePerformance> {
    void climbPerformance();
}
