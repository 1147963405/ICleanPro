package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.CleanReportBean;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface CleanReportService {
    List<CleanReportBean> selectSelective(Integer deviceId,Integer startIndex,Integer count);
    Future<List<CleanReportBean>> asyncCleanReport(Integer deviceId,Integer startIndex,Integer count);

    List<Map<String,Object>> selectCleanRportsByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectCleanRportsByParams(@Param("userId") Integer userId,@Param("cleanParams") String cleanParams);
    List<Map<String,Object>> selectCleanRports();
    List<Map<String,Object>> selectCleanRportsBySelective(@Param("cleanParams") String cleanParams);
    List<Map<String,Object>> selectCleanRportsTotal();
    List<Map<String,Object>> selectCleanRportsTotalByCustomerId(@Param("customerId") Integer customerId);
    List<Map<String,Object>> selectClearReportByDeviceId(Integer deviceId);


}
