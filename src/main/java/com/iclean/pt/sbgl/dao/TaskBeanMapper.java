package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.TaskBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskBean record);

    int insertSelective(TaskBean record);

    TaskBean selectByPrimaryKey(Integer id);
    List<TaskBean> selectBySelective(Integer deviceId, String workType);
    int updateByPrimaryKeySelective(TaskBean record);
    int updateByPrimaryKey(TaskBean record);

    List<TaskBean> selectSelective(Integer deviceId, Integer startIndex, Integer count);
    List<TaskBean> selectList(Integer startIndex, Integer count);


    List<Map<String,Object>> selectTasksByUserId(@Param("userId") Integer userId);
    List<Map<String,Object>> selectTasksByParams(@Param("userId") Integer userId, @Param("tasksParams") String params);
    List<Map<String,Object>> selectTasks();
    List<Map<String,Object>> selectTasksBySelective(@Param("tasksParams") String params);

    //out api
    List<Map<String,Object>>  selectTaskByDeviceId(Integer deviceId);

}