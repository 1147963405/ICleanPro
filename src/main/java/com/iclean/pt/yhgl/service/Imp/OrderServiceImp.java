package com.iclean.pt.yhgl.service.Imp;

import com.iclean.pt.yhgl.bean.OrderBean;
import com.iclean.pt.yhgl.dao.OrderBeanMapper;
import com.iclean.pt.yhgl.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderBeanMapper orderBeanMapper;

    @Override
    public List<OrderBean> selectBySelective(Integer customerId) {
        return orderBeanMapper.selectBySelective(customerId);
    }
}
