package com.iclean.pt.sbgl.service;

import com.iclean.pt.sbgl.bean.TaskBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface TaskService {
    List<TaskBean> selectBySelective(Integer deviceId,String workType);
    int insert(TaskBean record);
    TaskBean selectByPrimaryKey(Integer id);
    int updateByPrimaryKey(TaskBean record);
    int deleteByPrimaryKey(Integer id);
    List<TaskBean> selectSelective(Integer deviceId,Integer startIndex,Integer count);
    Future<List<TaskBean>> ayncSelective(Integer deviceId, Integer startIndex, Integer count);
    List<TaskBean> selectList(Integer startIndex,Integer count);

    List<Map<String,Object>> selectTasksByUserId(@Param("userId") Integer userId);
//    List<Map<String,Object>> selectTasksByParams(@Param("params") String params);
    List<Map<String,Object>> selectTasksByParams(@Param("userId") Integer userId,@Param("params") String params);
    List<Map<String,Object>> selectTasks();//selectTasksByName
    List<Map<String,Object>> selectTasksBySelective(@Param("params") String params);
}
