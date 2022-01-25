package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.TaskBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskBean record);

    int insertSelective(TaskBean record);

    TaskBean selectByPrimaryKey(Integer id);
    List<TaskBean> selectBySelective(Integer deviceId,String workType);
    int updateByPrimaryKeySelective(TaskBean record);
    int updateByPrimaryKey(TaskBean record);

    List<TaskBean> selectSelective(Integer deviceId,Integer startIndex,Integer count);
    List<TaskBean> selectList(Integer startIndex,Integer count);

}