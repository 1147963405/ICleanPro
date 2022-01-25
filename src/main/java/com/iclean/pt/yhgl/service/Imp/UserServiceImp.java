package com.iclean.pt.yhgl.service.Imp;


import com.iclean.pt.yhgl.bean.UserBean;
import com.iclean.pt.yhgl.dao.UserBeanMapper;
import com.iclean.pt.yhgl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserBeanMapper userBeanMapper;
    @Override
    public UserBean selectBySelective(String account, String passwd) {
        return userBeanMapper.selectBySelective(account,passwd);
    }


    @Override
    public int insertSelective(UserBean record) {
        return userBeanMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(UserBean record) {
        return userBeanMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userBeanMapper.deleteByPrimaryKey(id);
    }


    @Override
    public UserBean selectByPrimaryKey(Integer id) {
        return userBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserBean> queryListByageHelper() {
        return userBeanMapper.queryListByageHelper();
    }

    @Override
    public List<UserBean> queryBySelective(Integer start_index, Integer count) {
        return userBeanMapper.queryBySelective(start_index,count);
    }

    @Override
    public List<UserBean> queryByCustomerIdOrTypeId(Integer customerId,Integer typeId) {
        return userBeanMapper.queryByCustomerIdOrTypeId(customerId,typeId);
    }

    @Override
    public List<UserBean> queryByPage(Integer customerId, Integer typeId, Integer start_index, Integer count) {
        return userBeanMapper.queryByPage(customerId,typeId,start_index,count);
    }

}
