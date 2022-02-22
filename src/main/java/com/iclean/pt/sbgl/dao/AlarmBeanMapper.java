package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.AlarmBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlarmBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmBean record);

    int insertSelective(AlarmBean record);

    AlarmBean selectByPrimaryKey(Integer id);
    List<AlarmBean> selectBySelective(Integer deviceId, Integer status, Integer startIndex, Integer count);
    List<AlarmBean> selectList(Integer status, Integer startIndex, Integer count);
    int updateByPrimaryKeySelective(AlarmBean record);

    int updateByPrimaryKey(AlarmBean record);


//云平台 小程序
    List<Map<String,Object>> selectAlarmByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectAlarmsByParams(@Param("userId") Integer userId, @Param("alarmsParams") String params);
    List<Map<String,Object>> selectAlarms();
    List<Map<String,Object>> selectAlarmsBySelective(@Param("alarmsParams") String params);
//out api
    List<Map<String,Object>> selectAlarmByDeviceId(Integer deviceId);
}