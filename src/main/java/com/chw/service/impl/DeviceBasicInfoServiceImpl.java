package com.chw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.climb.DeviceBasicClimb;
import com.chw.constant.DeviceConstant;
import com.chw.entity.DeviceBasicInfo;
import com.chw.entity.DeviceCompanyType;
import com.chw.enums.DeviceEnum;
import com.chw.mapper.DeviceBasicInfoMapper;
import com.chw.service.IDeviceBasicInfoService;
import com.chw.service.IDeviceCompanyTypeService;
import com.chw.thread.SingleThreadPool;
import com.chw.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class DeviceBasicInfoServiceImpl extends ServiceImpl<DeviceBasicInfoMapper, DeviceBasicInfo> implements IDeviceBasicInfoService {

    @Autowired
    IDeviceCompanyTypeService companyTypeService;


    /**
     * 线程池并发爬取数据
     */
    @Override
    public void climbDeviceBasicInfo() {
        List<DeviceCompanyType> phoneComTypeList = companyTypeService.queryList();
        int phoneNum = 0;
        int computerNum = 0;
        CountDownLatch latch = new CountDownLatch(phoneComTypeList.size());
        for (DeviceCompanyType companyType : phoneComTypeList) {
            log.info("---------------------companyType:" + companyType.getCnName());
            String url;
            if (DeviceEnum.COMPUTER.getCode().equals(companyType.getDeviceType())) {
                url = DeviceConstant.COMPUTER_BASIC_URL;
                computerNum++;
            }
            else {
                url = DeviceConstant.PHONE_BASIC_URL;
                phoneNum++;
            }
            Runnable runnable = () -> {
                Integer proId = companyType.getProId();
                Integer page = 1;
                try {
                    while (true) {
                        List<DeviceBasicInfo> phoneBasicInfoList = DeviceBasicClimb.jsoupAnalysis(proId, page, url);
                        if (CollectionUtils.isEmpty(phoneBasicInfoList)) {
                            break;
                        }
                        page++;
                        phoneBasicInfoList.forEach(e -> e.setDeviceType(companyType.getDeviceType()));
                        saveOrUpdateBatch(phoneBasicInfoList);
                    }
                }
                catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                finally {
                    latch.countDown();
                }
            };
            SingleThreadPool.execute(runnable);
        }
//        SingleThreadPool.waitForEnd();
        try {
            latch.await();
        }
        catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.info("phoneNum:" + phoneNum);
        log.info("computerNum:" + computerNum);
    }
}
