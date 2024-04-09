package com.chw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.entity.LhcDataNum;
import com.chw.mapper.LhcDataNumMapper;
import com.chw.service.ILhcDataNumService;
import org.springframework.stereotype.Service;

@Service
public class LhcDataNumServiceImpl extends ServiceImpl<LhcDataNumMapper, LhcDataNum> implements ILhcDataNumService {
}
