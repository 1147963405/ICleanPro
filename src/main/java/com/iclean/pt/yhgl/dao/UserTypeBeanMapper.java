package com.iclean.pt.yhgl.dao;

import com.iclean.pt.yhgl.bean.UserTypeBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserTypeBeanMapper {

    List<UserTypeBean> selectList();
    UserTypeBean selectByPrimaryKey(Integer id);
    /*嵌套结果查询全部
     * 包括用户类型、用户信息、用户验证信息
     * */
//    List<UserTypeBean> selectAll();

    /*新增用户类型*/
    int insertSelective(UserTypeBean record);
    /*更新用户类型*/
    int updateByPrimaryKeySelective(UserTypeBean record);
    /*删除指定用户类型*/
    int deleteByPrimaryKey(Integer id);


}