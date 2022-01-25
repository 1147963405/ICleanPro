package com.iclean.pt.common.config.PageInterceptor;


import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/*分页插件配置*/
/*@Configuration
public class PageConfig {

    @Resource
    private List<SqlSessionFactory>  sqlSessionFactories;

    @PostConstruct
    public void init(){
        PageInterceptor pageInterceptor=new PageInterceptor();
        Properties properties=new Properties();
        *//*如果不配置，会自动检测所用数据库类型*//*
        properties.put("helperDialect","mysql");
        *//*默认为false，分页合理化参数*//*
        properties.put("reasonable","true");
        *//*默认为false，多数据源使用*//*
        properties.put("autoRuntimeDialect","false");
        *//*默认为false，true：如果pagesize=0就会自动查询全部结果，下拉框的查询可以使用*//*
        properties.put("pageSizeZero","true");
        *//*为了支持startpage（object params）方法*//*
        properties.put("params","pageNum=pageNow;count=executorCount");
        pageInterceptor.setProperties(properties);
        for (SqlSessionFactory s:sqlSessionFactories) {
            s.getConfiguration().addInterceptor(pageInterceptor);
        }
    }
}*/
