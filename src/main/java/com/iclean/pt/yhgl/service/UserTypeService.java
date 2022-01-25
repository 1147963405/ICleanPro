package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.UserTypeBean;

import java.util.List;

public interface UserTypeService {
    List<UserTypeBean> selectList();
    UserTypeBean selectByPrimaryKey(Integer id);
//    List<UserTypeBean> selectAll();
    int insertSelective(UserTypeBean record);
    int updateByPrimaryKeySelective(UserTypeBean record);
    int deleteByPrimaryKey(Integer id);
}
