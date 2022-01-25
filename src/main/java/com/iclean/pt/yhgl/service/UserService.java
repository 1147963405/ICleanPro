package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.UserBean;

import java.util.List;

public interface UserService {


    UserBean selectBySelective(String account, String passwd);
//    List<UserBean> queryUsersByageHelper(Integer customerId, Integer typeId);
    int insertSelective(UserBean record);
    int updateByPrimaryKeySelective(UserBean record);
    int deleteByPrimaryKey(Integer id);
    UserBean selectByPrimaryKey(Integer id);
    List<UserBean> queryListByageHelper();
    List<UserBean> queryBySelective(Integer start_index, Integer count);
    /*获取角色模块和分组模块列表*/
    List<UserBean> queryByCustomerIdOrTypeId(Integer customerId,Integer typeId);
    List<UserBean> queryByPage(Integer customerId,Integer typeId,Integer start_index, Integer count);
}
