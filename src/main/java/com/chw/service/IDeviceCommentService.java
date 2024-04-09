package com.chw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.entity.DeviceComment;

public interface IDeviceCommentService extends IService<DeviceComment> {

    boolean removeByDeviceId(Integer deviceId);

    void climbComment();
}
