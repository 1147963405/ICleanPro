package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.UserAuthorityBean;
import org.apache.ibatis.annotations.Mapper;



public interface UserAuthorityService {
    UserAuthorityBean selectByUserTypeId(Integer id);
    UserAuthorityBean selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(UserAuthorityBean record);
}
