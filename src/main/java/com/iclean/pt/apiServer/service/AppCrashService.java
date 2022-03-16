package com.iclean.pt.apiServer.service;

import com.iclean.pt.apiServer.bean.AppCrashBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppCrashService {

       int insert(AppCrashBean record);
       List<AppCrashBean> selectBySerial(@Param("serial") String serial);


}
