package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.AlarmBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface AlarmBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AlarmBean record);

    int insertSelective(AlarmBean record);

    AlarmBean selectByPrimaryKey(Integer id);
    List<AlarmBean> selectBySelective(Integer deviceId,Integer status,Integer startIndex,Integer count);
    List<AlarmBean> selectList(Integer status,Integer startIndex,Integer count);
    int updateByPrimaryKeySelective(AlarmBean record);

    int updateByPrimaryKey(AlarmBean record);
}