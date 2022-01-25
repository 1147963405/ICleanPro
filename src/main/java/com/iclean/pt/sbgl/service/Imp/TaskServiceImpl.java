package com.iclean.pt.sbgl.service.Imp;

import com.iclean.pt.sbgl.bean.TaskBean;
import com.iclean.pt.sbgl.dao.TaskBeanMapper;
import com.iclean.pt.sbgl.service.TaskService;
import com.iclean.pt.yhgl.bean.CustomerDeviceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskBeanMapper taskBeanMapper;

    @Override
    public  List<TaskBean> selectBySelective(Integer deviceId,String workType) {
        return taskBeanMapper.selectBySelective(deviceId,workType);
    }

    @Override
    public int insert(TaskBean record) {
        return taskBeanMapper.insert(record);
    }

    @Override
    public TaskBean selectByPrimaryKey(Integer id) {
        return taskBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKey(TaskBean record) {
        return taskBeanMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return taskBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<TaskBean> selectSelective(Integer deviceId, Integer startIndex, Integer count) {
        return taskBeanMapper.selectSelective(deviceId,startIndex,count);
    }

    @Override
    @Async("myTaskExecutor")
    public Future<List<TaskBean>> ayncSelective(Integer deviceId, Integer startIndex, Integer count) {
        return new AsyncResult<List<TaskBean>>(taskBeanMapper.selectSelective(deviceId,startIndex,count));
    }

    @Override
    public List<TaskBean> selectList(Integer startIndex, Integer count) {
        return taskBeanMapper.selectList(startIndex,count);
    }
}
