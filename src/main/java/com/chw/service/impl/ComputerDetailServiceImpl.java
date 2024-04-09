package com.chw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.entity.ComputerDetail;
import com.chw.mapper.ComputerDetailMapper;
import com.chw.service.IComputerDetailService;
import org.springframework.stereotype.Service;

@Service
public class ComputerDetailServiceImpl extends ServiceImpl<ComputerDetailMapper, ComputerDetail> implements IComputerDetailService {
}
