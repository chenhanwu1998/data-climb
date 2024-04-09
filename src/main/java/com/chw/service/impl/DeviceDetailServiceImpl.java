package com.chw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.entity.DeviceDetail;
import com.chw.mapper.DeviceDetailMapper;
import com.chw.service.IDeviceDetailService;
import org.springframework.stereotype.Service;

@Service
public class DeviceDetailServiceImpl extends ServiceImpl<DeviceDetailMapper, DeviceDetail> implements IDeviceDetailService {
}
