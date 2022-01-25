package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.CustomerGroupBean;
import com.iclean.pt.yhgl.dao.CustomerGroupBeanMapper;
import com.iclean.pt.yhgl.service.CustomerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerGroupServiceImp implements CustomerGroupService {

    @Autowired
    private CustomerGroupBeanMapper customerGroupBeanMapper;
    @Override
    public List<CustomerGroupBean> selectByPrimaryKey(Integer pId) {
        return customerGroupBeanMapper.selectByPrimaryKey(pId);
    }

    @Override
    public CustomerGroupBean queryByPrimaryKey(Integer id) {
        return customerGroupBeanMapper.queryByPrimaryKey(id);
    }

    @Override
    public int insertSelective(CustomerGroupBean record) {
        return customerGroupBeanMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(CustomerGroupBean record) {
        return customerGroupBeanMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return customerGroupBeanMapper.deleteByPrimaryKey(id);
    }
}
