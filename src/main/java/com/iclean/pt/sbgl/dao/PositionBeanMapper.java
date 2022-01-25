package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.PositionBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PositionBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PositionBean record);

    int insertSelective(PositionBean record);

    List<PositionBean> selectBySelective(Integer mapId, Integer deviceId);

    PositionBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PositionBean record);

    int updateByPrimaryKey(PositionBean record);
}