package com.chw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.entity.ComputerParam;
import com.chw.mapper.ComputerParamMapper;
import com.chw.service.IComputerParamService;
import org.springframework.stereotype.Service;

@Service
public class ComputerParamServiceImpl extends ServiceImpl<ComputerParamMapper, ComputerParam> implements IComputerParamService {
}
