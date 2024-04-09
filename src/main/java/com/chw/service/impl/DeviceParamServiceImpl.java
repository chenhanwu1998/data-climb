package com.chw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.climb.DeviceDetailClimb;
import com.chw.entity.ComputerDetail;
import com.chw.entity.ComputerParam;
import com.chw.entity.DeviceBasicInfo;
import com.chw.entity.DeviceDetail;
import com.chw.entity.DeviceParam;
import com.chw.enums.DeviceEnum;
import com.chw.mapper.DeviceParamMapper;
import com.chw.service.IComputerDetailService;
import com.chw.service.IComputerParamService;
import com.chw.service.IDeviceBasicInfoService;
import com.chw.service.IDeviceCommentService;
import com.chw.service.IDeviceDetailService;
import com.chw.service.IDeviceParamService;
import com.chw.service.IDevicePerformanceService;
import com.chw.thread.SingleThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DeviceParamServiceImpl extends ServiceImpl<DeviceParamMapper, DeviceParam> implements IDeviceParamService {

    @Autowired
    IDeviceBasicInfoService basicInfoService;
    @Autowired
    IDeviceDetailService deviceDetailService;
    @Autowired
    IComputerDetailService computerDetailService;
    @Autowired
    IComputerParamService computerParamService;
    @Autowired
    IDevicePerformanceService devicePerformanceService;
    @Autowired
    IDeviceCommentService deviceCommentService;

    @Override
    public void climbDeviceDetailAndParam() {
        List<DeviceBasicInfo> phoneBasicInfoList = basicInfoService.list();
        for (DeviceBasicInfo basicInfo : phoneBasicInfoList) {
            if (!basicInfo.getCommentUrl().contains("review")) {
                log.info("错误的评论连接跳过");
                continue;
            }
            Runnable runnable = () -> {
                DeviceDetailClimb mobileDetailClimb = new DeviceDetailClimb();
                Object[] objects = new Object[0];
                try {
                    objects = mobileDetailClimb.getDeviceDetail(basicInfo.getCommentUrl());
                }
                catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                if (null == objects) {
                    log.info("参数数据为空，跳过");
                    return;
                }
                if (DeviceEnum.COMPUTER.getCode().equals(basicInfo.getDeviceType())) {
                    if (!CollectionUtils.isEmpty((Map) objects[0])) {
                        ComputerDetail computerDetail = JSONObject.parseObject(JSONObject.toJSONString(objects[0]), ComputerDetail.class);
                        computerDetail.setDeviceId(basicInfo.getId());
                        computerDetailService.saveOrUpdate(computerDetail);
                    }
                    if (!CollectionUtils.isEmpty((Map) objects[1])) {
                        ComputerParam computerParam = JSONObject.parseObject(JSONObject.toJSONString(objects[1]), ComputerParam.class);
                        computerParam.setDeviceId(basicInfo.getId());
                        computerParamService.saveOrUpdate(computerParam);
                    }
                }
                else {
                    if (!CollectionUtils.isEmpty((Map) objects[0])) {
                        DeviceDetail deviceDetail = JSONObject.parseObject(JSONObject.toJSONString(objects[0]), DeviceDetail.class);
                        deviceDetail.setDeviceId(basicInfo.getId());
                        deviceDetailService.saveOrUpdate(deviceDetail);
                    }
                    if (!CollectionUtils.isEmpty((Map) objects[1])) {
                        DeviceParam deviceParam = JSONObject.parseObject(JSONObject.toJSONString(objects[1]), DeviceParam.class);
                        deviceParam.setDeviceId(basicInfo.getId());
                        this.saveOrUpdate(deviceParam);
                    }
                }
            };
            SingleThreadPool.execute(runnable);
        }
        SingleThreadPool.waitForEnd();
    }
}
