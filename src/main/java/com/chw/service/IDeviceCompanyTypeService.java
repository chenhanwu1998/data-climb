package com.chw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.entity.DeviceCompanyType;

import java.util.List;

public interface IDeviceCompanyTypeService extends IService<DeviceCompanyType> {

    void climbCpmpanyTypeInfo();

    List<DeviceCompanyType> queryList();
}
