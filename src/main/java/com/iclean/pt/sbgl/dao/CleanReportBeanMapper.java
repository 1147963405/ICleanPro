package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.CleanReportBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CleanReportBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CleanReportBean record);

    int insertSelective(CleanReportBean record);

    CleanReportBean selectByPrimaryKey(Integer id);
//    List<CleanReportBean> selectSelective(Integer deviceId);
    List<CleanReportBean> selectSelective(Integer deviceId,Integer startIndex,Integer count);
    int updateByPrimaryKeySelective(CleanReportBean record);

    int updateByPrimaryKey(CleanReportBean record);
}