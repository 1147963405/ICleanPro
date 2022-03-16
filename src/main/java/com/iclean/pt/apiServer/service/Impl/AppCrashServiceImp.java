package com.iclean.pt.apiServer.service.Impl;

import com.iclean.pt.apiServer.bean.AppCrashBean;
import com.iclean.pt.apiServer.dao.AppCrashBeanMapper;
import com.iclean.pt.apiServer.service.AppCrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppCrashServiceImp implements AppCrashService {

    @Autowired
    private AppCrashBeanMapper appCrashBeanMapper;

    public List<AppCrashBean> selectBySerial(String serial){

        return appCrashBeanMapper.selectBySerial(serial);
    }

    public int insert(AppCrashBean record){

        return appCrashBeanMapper.insert(record);
    }
}
