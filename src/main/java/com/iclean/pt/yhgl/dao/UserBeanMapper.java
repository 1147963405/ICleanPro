package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserBeanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBean record);

    int insertSelective(UserBean record);

    /*通过用户id获取用户*/
    UserBean selectByPrimaryKey(Integer id);
    /*通过用户账号和密码登录*/
    UserBean selectBySelective(String account,String passwd);
    /*查询用户列表*/
    List<UserBean> queryListByageHelper();
    List<UserBean> queryBySelective(Integer start_index, Integer count);
   /*通过客户id和用户类型获取对应的用户列表  queryUsersByPage*/
    int updateByPrimaryKeySelective(UserBean record);
    int updateByPrimaryKey(UserBean record);

    /*获取角色模块和分组模块列表*/
    List<UserBean> queryByCustomerIdOrTypeId(Integer customerId,Integer typeId);
    List<UserBean> queryByPage(Integer customerId,Integer typeId,Integer start_index, Integer count);
}