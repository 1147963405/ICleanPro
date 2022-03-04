package com.iclean.apiServer.service.Impl;

import com.iclean.apiServer.bean.AppCrashBean;
import com.iclean.apiServer.dao.AppCrashBeanMapper;
import com.iclean.apiServer.service.AppCrashService;
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
