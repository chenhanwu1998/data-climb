package com.chw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.entity.DeviceParam;

public interface IDeviceParamService extends IService<DeviceParam> {

    void climbDeviceDetailAndParam();
}
