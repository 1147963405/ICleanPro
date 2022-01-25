package com.iclean.pt.sbgl.dao;

import com.iclean.pt.sbgl.bean.PathsBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PathsBeanMapper {

    int deleteByPrimaryKey(Integer id,String name,Integer mapId,Integer deviceId);
    List<PathsBean> deleteByMapIdAndDeviceId(Integer mapId,Integer deviceId);
    int insert(PathsBean record);

    int insertSelective(PathsBean record);

    PathsBean selectByPrimaryKey(Integer id,String name);
    List<PathsBean> selectBySelective(Integer mapId);
    List<PathsBean> selectByMapIdAndDeviceId(Integer mapId,Integer deviceId);

    int updateByPrimaryKeySelective(PathsBean record);
    int updateByPrimaryKey(PathsBean record);

}