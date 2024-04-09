package com.chw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.climb.DeviceBasicClimb;
import com.chw.entity.DeviceCompanyType;
import com.chw.enums.DeviceEnum;
import com.chw.mapper.DeviceCompanyTypeMapper;
import com.chw.service.IDeviceCompanyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceCompanyTypeServiceImpl extends ServiceImpl<DeviceCompanyTypeMapper, DeviceCompanyType> implements IDeviceCompanyTypeService {


    @Override
    public void climbCpmpanyTypeInfo() {
        DeviceBasicClimb mobileBasicClimb = new DeviceBasicClimb();
        List<DeviceCompanyType> list = mobileBasicClimb.getPhoneCompanyBasicInfo();
        list.forEach(e -> {
            e.setDeviceType(DeviceEnum.PHONE.getCode());
            e.setProId(e.getId());
            e.setId(null);
        });
        this.removeByDeviceType(DeviceEnum.PHONE.getCode());
        this.saveBatch(list);

        List<DeviceCompanyType> computerCompanyTypeList = mobileBasicClimb.getComputerCompanyBasicInfo();
        computerCompanyTypeList.forEach(e -> {
            e.setDeviceType(DeviceEnum.COMPUTER.getCode());
            e.setProId(e.getId());
            e.setId(null);
        });
        this.removeByDeviceType(DeviceEnum.COMPUTER.getCode());
        this.saveBatch(computerCompanyTypeList);
    }

    public void removeByDeviceType(String deviceType) {
        LambdaQueryWrapper<DeviceCompanyType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DeviceCompanyType::getDeviceType, deviceType);
        this.remove(wrapper);
    }

    @Override
    public List<DeviceCompanyType> queryList() {
        return this.list();
    }
}
