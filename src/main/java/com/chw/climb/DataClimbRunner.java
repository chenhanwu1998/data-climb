package com.chw.climb;

import com.chw.service.IDeviceBasicInfoService;
import com.chw.service.IDeviceCommentService;
import com.chw.service.IDeviceCompanyTypeService;
import com.chw.service.IDeviceParamService;
import com.chw.service.IDevicePerformanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 数据获取核心类
 */
@Slf4j
@Order(3)
@Component
public class DataClimbRunner implements ApplicationRunner {

    @Autowired
    private IDeviceCompanyTypeService deviceCompanyTypeService;
    @Autowired
    private IDeviceBasicInfoService deviceBasicInfoService;
    @Autowired
    private IDeviceParamService deviceParamService;
    @Autowired
    private IDeviceCommentService deviceCommentService;
    @Autowired
    private IDevicePerformanceService devicePerformanceService;


    /**
     * 核心方法接入点
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) {
//        log.info("系统初始化之后执行");
//        deviceCompanyTypeService.climbCpmpanyTypeInfo();
//        log.info("结束获取company基础信息");
//        deviceBasicInfoService.climbDeviceBasicInfo();
//        log.info("结束获取基础信息");
//        deviceParamService.climbDeviceDetailAndParam();
//        log.info("获取详情参数结束");
//        devicePerformanceService.climbPerformance();
//        log.info("获取性能参数结束");
//        deviceCommentService.climbComment();
//        log.info("获取评论结束");
    }

}
