package com.chw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.climb.DeviceDetailClimb;
import com.chw.entity.DeviceBasicInfo;
import com.chw.entity.DevicePerformance;
import com.chw.mapper.DevicePerformanceMapper;
import com.chw.service.IDeviceBasicInfoService;
import com.chw.service.IDevicePerformanceService;
import com.chw.thread.SingleThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DevicePerformanceServiceImpl extends ServiceImpl<DevicePerformanceMapper, DevicePerformance> implements IDevicePerformanceService {

    @Autowired
    IDeviceBasicInfoService deviceBasicInfoService;

    @Override
    public void climbPerformance() {
        List<DeviceBasicInfo> phoneBasicInfoList = deviceBasicInfoService.list();
        DeviceDetailClimb deviceDetailClimb = new DeviceDetailClimb();
        for (DeviceBasicInfo basicInfo : phoneBasicInfoList) {
            if (!basicInfo.getCommentUrl().contains("review")) {
                log.info("错误的评论连接跳过");
                continue;
            }
            Runnable runnable = () -> {
                try {
                    Map<String, String> performanceMap = deviceDetailClimb.getDevicePerformanceDetail(basicInfo.getCommentUrl());
                    DevicePerformance performance = JSONObject.parseObject(JSONObject.toJSONString(performanceMap), DevicePerformance.class);
                    performance.setDeviceId(basicInfo.getId());
                    this.saveOrUpdate(performance);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            };
            SingleThreadPool.execute(runnable);
        }
        SingleThreadPool.waitForEnd();

    }
}
