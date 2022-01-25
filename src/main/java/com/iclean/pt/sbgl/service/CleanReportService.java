package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.CleanReportBean;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;

import java.util.List;
import java.util.concurrent.Future;

public interface CleanReportService {
    List<CleanReportBean> selectSelective(Integer deviceId,Integer startIndex,Integer count);
    Future<List<CleanReportBean>> asyncCleanReport(Integer deviceId,Integer startIndex,Integer count);
}
