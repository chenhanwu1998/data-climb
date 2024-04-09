package com.chw.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.climb.DeviceDetailClimb;
import com.chw.entity.DeviceBasicInfo;
import com.chw.entity.DeviceComment;
import com.chw.mapper.DeviceCommentMapper;
import com.chw.service.IDeviceBasicInfoService;
import com.chw.service.IDeviceCommentService;
import com.chw.thread.SingleThreadPool;
import com.chw.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceCommentServiceImpl extends ServiceImpl<DeviceCommentMapper, DeviceComment> implements IDeviceCommentService {

    @Autowired
    IDeviceBasicInfoService deviceBasicInfoService;

    @Override
    public boolean removeByDeviceId(Integer deviceId) {
        if (null == deviceId) {
            return false;
        }
        DeviceComment record = new DeviceComment();
        record.setDeviceId(deviceId);
        return this.remove(this.getWrapper(record));
    }

    @Override
    public void climbComment() {
        List<DeviceBasicInfo> phoneBasicInfoList = deviceBasicInfoService.list();
        DeviceDetailClimb deviceDetailClimb = new DeviceDetailClimb();
        for (DeviceBasicInfo basicInfo : phoneBasicInfoList) {
            Runnable runnable = () -> {
                List<Map<String, String>> commentList = deviceDetailClimb.getDeviceComment(basicInfo.getId());
                List<DeviceComment> deviceCommentList = JSONObject.parseArray(JSONObject.toJSONString(commentList), DeviceComment.class);
                deviceCommentList.forEach(e -> e.setDeviceId(basicInfo.getId()));
                if (CollectionUtils.isNotEmpty(deviceCommentList)) {
                    this.removeByDeviceId(basicInfo.getId());
                    this.saveBatch(deviceCommentList);
                }
            };
            SingleThreadPool.execute(runnable);
        }
        SingleThreadPool.waitForEnd();

    }

    private LambdaQueryWrapper<DeviceComment> getWrapper(DeviceComment deviceComment) {
        LambdaQueryWrapper<DeviceComment> wrapper = Wrappers.lambdaQuery();
        if (null != deviceComment.getDeviceId()) {
            wrapper.eq(DeviceComment::getDeviceId, deviceComment.getDeviceId());
        }
        return wrapper;
    }
}
