package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.CleanReportBean;
import com.iclean.pt.sbgl.dao.CleanReportBeanMapper;
import com.iclean.pt.sbgl.service.CleanReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class CleanReportServiceImpl implements CleanReportService {

    @Autowired
    private CleanReportBeanMapper cleanReportBeanMapper;

    @Override
    public List<CleanReportBean> selectSelective(Integer deviceId,Integer startIndex,Integer count) {
        return cleanReportBeanMapper.selectSelective(deviceId,startIndex,count);
    }

    @Override
    @Async("myTaskExecutor")
    public Future<List<CleanReportBean>> asyncCleanReport(Integer deviceId, Integer startIndex, Integer count) {
        return new AsyncResult<List<CleanReportBean>>(cleanReportBeanMapper.selectSelective(deviceId,startIndex,count));
    }
}
