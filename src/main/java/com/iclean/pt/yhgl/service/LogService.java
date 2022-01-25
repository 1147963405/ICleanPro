package com.iclean.pt.yhgl.service;

import com.iclean.pt.yhgl.bean.LogBean;

public interface LogService {
    LogBean selectByUserId(Integer uid);
    LogBean selectByCustomerId(Integer cid);
}
