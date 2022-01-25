/*
package com.iclean.pt.demo.service.Imp;

import com.iclean.pt.demo.bean.UserBean;
import com.iclean.pt.demo.dao.UserBeanMapper;
import com.iclean.pt.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserBeanMapper userBeanMapper;

    @Override
    public UserBean selectByPrimaryKey(Integer id) {

        return userBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UserBean> selectAll() {

        List<UserBean> userBeanWithBLOBs = userBeanMapper.selectAll();
        System.out.println(userBeanWithBLOBs);
        return userBeanWithBLOBs;
    }
}
*/
