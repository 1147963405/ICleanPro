package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.CustomerBean;
import com.iclean.pt.yhgl.bean.CustomerGroupBean;
import com.iclean.pt.yhgl.dao.CustomerBeanMapper;
import com.iclean.pt.yhgl.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {


    @Autowired
    private CustomerBeanMapper customerBeanMapper;


    @Override
    public int insertSelective(CustomerBean record) {
        return customerBeanMapper.insertSelective(record);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return customerBeanMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(CustomerBean record) {
        return customerBeanMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public CustomerBean selectByPrimaryKey(Integer id) {
        return customerBeanMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CustomerBean> selectBySelective(Integer customerGroupId,Integer customerId) {
        return customerBeanMapper.selectBySelective(customerGroupId,customerId);
    }


}
