package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.UserAuthorityBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAuthorityBeanMapper {
   /* int deleteByPrimaryKey(Integer id);

    int insert(UserAuthorityBean record);

    int insertSelective(UserAuthorityBean record);
*/
    UserAuthorityBean selectByPrimaryKey(Integer id);
    UserAuthorityBean selectByUserTypeId(Integer id);
    int updateByPrimaryKeySelective(UserAuthorityBean record);

    /* int updateByPrimaryKey(UserAuthorityBean record);*/
}