package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.UserTypeBean;
import com.iclean.pt.yhgl.dao.UserTypeBeanMapper;
import com.iclean.pt.yhgl.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeServiceImp implements UserTypeService {

    @Autowired
    private UserTypeBeanMapper userTypeBeanMapper;

    @Override
    public List<UserTypeBean> selectList() {
        return userTypeBeanMapper.selectList();
    }

    @Override
    public UserTypeBean selectByPrimaryKey(Integer id) {
        return userTypeBeanMapper.selectByPrimaryKey(id);
    }

    /*@Override
    public List<UserTypeBean> selectAll() {
        return userTypeBeanMapper.selectAll();
    }*/

    @Override
    public int insertSelective(UserTypeBean record) {

//        int count = userTypeBeanMapper.insertSelective(record);

        return userTypeBeanMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(UserTypeBean record) {
        return userTypeBeanMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userTypeBeanMapper.deleteByPrimaryKey(id);
    }
}
