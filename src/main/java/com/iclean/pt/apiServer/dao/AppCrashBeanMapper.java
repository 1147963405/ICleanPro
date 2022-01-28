package com.iclean.pt.apiServer.dao;

import com.iclean.pt.apiServer.bean.AppCrashBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppCrashBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppCrashBean record);

    int insertSelective(AppCrashBean record);

    List<AppCrashBean> selectBySerial(@Param("serial") String serial);

    int updateByPrimaryKeySelective(AppCrashBean record);

    int updateByPrimaryKey(AppCrashBean record);
}