package com.iclean.pt.common.config.DBConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

//@Component
public class DBServletContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger =  LoggerFactory.getLogger(DBServletContextListener.class);

//    @Autowired
    private JdbcTemplate jdbcTemplate;


    public static ServletContext servletContext;

//    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        String sql="select * from tb_user" ;
        String str="select * from tb_maps where type=0";
        List<Map<String, Object>> user = jdbcTemplate.queryForList(sql);
//        JSONArray jsonArray = new JSONArray().fromObject(maps);
        // 将 ApplicationContext 转化为 WebApplicationContext
        WebApplicationContext webApplicationContext =
                (WebApplicationContext)contextRefreshedEvent.getApplicationContext();
        // 从 webApplicationContext 中获取  servletContext
        ServletContext servletContext = webApplicationContext.getServletContext();
        this.servletContext = servletContext;
        // servletContext设置值
        servletContext.setAttribute("user", user);
    }


}
