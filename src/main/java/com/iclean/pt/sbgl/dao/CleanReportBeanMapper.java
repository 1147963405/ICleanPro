package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.CleanReportBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CleanReportBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CleanReportBean record);

    int insertSelective(CleanReportBean record);

    CleanReportBean selectByPrimaryKey(Integer id);
//    List<CleanReportBean> selectSelective(Integer deviceId);
    List<CleanReportBean> selectSelective(Integer deviceId, Integer startIndex, Integer count);
    int updateByPrimaryKeySelective(CleanReportBean record);
    int updateByPrimaryKey(CleanReportBean record);

    List<Map<String,Object>> selectCleanRportsByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectCleanRportsByParams(@Param("userId") Integer userId, @Param("cleanParams") String cleanParams);
    List<Map<String,Object>> selectCleanRports();
    List<Map<String,Object>> selectCleanRportsBySelective(@Param("cleanParams") String cleanParams);//selectCleanRportsTotal
    List<Map<String,Object>> selectCleanRportsTotal();
    List<Map<String,Object>> selectCleanRportsTotalByCustomerId(@Param("customerId") Integer customerId);
}