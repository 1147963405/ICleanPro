package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.UserAuthorityBean;
import com.iclean.pt.yhgl.dao.UserAuthorityBeanMapper;
import com.iclean.pt.yhgl.service.UserAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

    @Autowired
    private UserAuthorityBeanMapper userAuthorityBeanMapper;


    @Override
    public UserAuthorityBean selectByUserTypeId(Integer id) {
        return userAuthorityBeanMapper.selectByUserTypeId(id);
    }

    @Override
    public UserAuthorityBean selectByPrimaryKey(Integer id) {
        return userAuthorityBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserAuthorityBean record) {
        return userAuthorityBeanMapper.updateByPrimaryKeySelective(record);
    }
}
