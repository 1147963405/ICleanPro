package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.LogBean;
import com.iclean.pt.yhgl.dao.LogBeanMapper;
import com.iclean.pt.yhgl.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogBeanMapper logBeanMapper;

    @Override
    public LogBean selectByUserId(Integer uid) {
        return logBeanMapper.selectByUserId(uid);
    }

    @Override
    public LogBean selectByCustomerId(Integer cid) {
        return null;
    }
}
