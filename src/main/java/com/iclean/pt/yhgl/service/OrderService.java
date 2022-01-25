package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.OrderBean;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /*通过客户id获取客户订单列表*/
    List<OrderBean> selectBySelective(Integer customerId);
}
