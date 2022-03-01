package com.iclean.pt.sbgl.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iclean.pt.sbgl.bean.MapsBean;
import com.iclean.pt.sbgl.bean.PathsBean;
import com.iclean.pt.sbgl.bean.ReportJsonBean;
import com.iclean.pt.sbgl.service.CleanReportService;
import com.iclean.pt.sbgl.service.MapsService;
import com.iclean.pt.sbgl.service.PathsService;
import com.iclean.pt.utils.*;
import com.iclean.pt.yhgl.service.CustomerService;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.parseInt;

@RestController
public class CleanReportController {

    private final static Logger logger = LoggerFactory.getLogger(CleanReportController.class);

    @Autowired
    private CleanReportService cleanReportService;
    @Autowired
    private CommUtil commUtil;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PathsService pathsService;
    @Autowired
    private MapsService mapsService;

    /**
     * @param
     * @param
     * @return Result
     * @description 获取清洁报告列表
     **/
    @PostMapping(value = "/clean_report/info")
//    public Result getCleanReportList(@RequestBody Map<String,Object> cleanReportMap) {
    public Result getCleanReportList(@RequestParam Map<String,Object> cleanReportMap) {
 /*
 获取清洁报告{start_index=0, count=20, count_flag=true, beginTime=, endTime=, user_id=1}
 {start_index=0, count=20, count_flag=true, beginTime=, endTime=, user_id=1}
{"user_id":1,"start_index":0,"count":10,"count_flag":true,"beginTime":"","endTime":""}:
* */
        logger.info("获取清洁报告"+cleanReportMap);
        JSONObject jsonObj = commUtil.getJson(cleanReportMap);
        int start_index = parseInt(jsonObj.get("start_index").toString());
        int count = parseInt(jsonObj.get("count").toString());
        int user_id = parseInt(jsonObj.get("user_id").toString());
        List<Map<String,Object>> cleanReportLst =null;
        int counts = customerService.selectCustomer(user_id);
        if(counts==0){
            if(jsonObj.get("device_name")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and  t.name like '%"+jsonObj.get("device_name")+"%'");
                PageHelper.offsetPage(start_index, count);
                cleanReportLst=cleanReportService.selectCleanRportsBySelective(deviceNameSql.toString());
            }else if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                StringBuilder timeSql = new StringBuilder();
                timeSql.append(" and   FROM_UNIXTIME(r.update_time, '%Y-%m-%d')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                PageHelper.offsetPage(start_index, count);
                cleanReportLst=cleanReportService.selectCleanRportsBySelective(timeSql.toString());
            }else {
                PageHelper.offsetPage(start_index, count);
                cleanReportLst=cleanReportService.selectCleanRports();
            }
        }else {
            if(jsonObj.get("device_name")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and  t.name like '%"+jsonObj.get("device_name")+"%'");
                PageHelper.offsetPage(start_index, count);
                cleanReportLst=cleanReportService.selectCleanRportsByParams(user_id,deviceNameSql.toString());
            }else if(!jsonObj.get("beginTime").equals("") || !jsonObj.get("endTime").equals("")){
                StringBuilder timeSql = new StringBuilder();
                timeSql.append("  and   FROM_UNIXTIME(r.update_time, '%Y-%m-%d ')  between '"+jsonObj.get("beginTime")+"'  and  '"+jsonObj.get("endTime")+"'");
                PageHelper.offsetPage(start_index, count);
                cleanReportLst=cleanReportService.selectCleanRportsByParams(user_id,timeSql.toString());
    }else {
        PageHelper.offsetPage(start_index, count);
        cleanReportLst=cleanReportService.selectCleanRportsByUserId(user_id);
    }
}

        for (Map<String,Object> m:cleanReportLst) {
            String map_path= Constants.Global.IMAGES_URL.getValue()  + "map/" + m.get("device_id") + "/" +  m.get("uuid")+ "/map.png";
            //http://47.92.192.154:9077/iclean-cloud/data/download/report_path?device_id=209&file_name=7daa2cc5-c4c3-4d71-b8bd-534a2f897051
            JSONObject report = JSONObject.parseObject(m.get("report").toString());
            ReportJsonBean reportJsonBean = BeanUtil.copyProperties(report, ReportJsonBean.class);
            if(reportJsonBean.getPathFile()!=null){
                String pathList=Constants.Global.PATH_URL.getValue()+Constants.Global.REPORT_DOWNLOAD_PATH.getValue()+"?device_id="+m.get("device_id")+"&file_name="+reportJsonBean.getPathFile();
                reportJsonBean.setPath_list(pathList);
            }else {
                reportJsonBean.setPathFile("");
//                reportJsonBean.setPath_list("[]");
            }
            m.put("map_path",map_path);
            m.put("report",reportJsonBean);
//            logger.info("reportJsonBean:"+reportJsonBean);
        }
        long total = ((com.github.pagehelper.Page) cleanReportLst).getTotal();
        Map<String,Object> maps=new ConcurrentHashMap<>();
        maps.put("count", total);
        maps.put("reports",cleanReportLst);
        return  Result.ok().data(maps).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 添加路径
     **/
    @PostMapping(value = "/path/add")
    public Result addPaths() {

        /*
         * 步骤：
         * 1.通过mqtt订阅的主题iclean/robot/messageEx获取到设备新增的地图信息
         * 2.通过device_id获取tb_maps表中的map_id
         * 3.通过map封装
         * */
        /*1*/
        logger.info("添加路径","功能待定。。。。。");
        JSONObject jb = JSON.parseObject("");
        /*2*/
        MapsBean mapsBean = mapsService.selectBySelective((Integer) jb.get("device_id"), String.valueOf(jb.get("map_name")));
        /*组装实体PathsBean*/
        PathsBean pathsBean=new PathsBean();
        pathsBean.setPath(JSONObject.toJSONString(jb.get("msg")));
        pathsBean.setType((Integer) jb.get("type"));
        pathsBean.setName( String.valueOf(jb.get("origin_name")));
        pathsBean.setDeviceId((Integer) jb.get("device_id"));
        pathsBean.setMapId(mapsBean.getId());
        pathsBean.setUpdateTime(  new Date().getTime());
        int insert = pathsService.insert(pathsBean);
        if(insert>0){
            return Result.ok().msg("路径添加成功");
        }
        return Result.ok().msg("路径添加失败");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 修改路径
     **/
    @PostMapping(value = "/path/update")
    public Result updatePaths() {

        /*步骤：
         * 1.通过mqtt获取paths名称并查询数据库表tb_paths获取指定路径实体bean
         * 2.更新mqtt中已变更的指定路径信息
         * 3.*/
        /*1*/
        logger.info("修改路径","功能未做。。。。。");
        JSONObject jb = JSON.parseObject("");
        /*通过map_id和path_name获取指定地图路径*/
        PathsBean pathsBean = pathsService.selectByPrimaryKey(null, String.valueOf(jb.get("origin_name")));
//        System.out.println("pathsBean: "+pathsBean);
        /*2*/
        pathsBean.setPath(JSONObject.toJSONString(jb.get("msg")));
        pathsBean.setType((Integer) jb.get("type"));
        pathsBean.setName( String.valueOf(jb.get("new_name")));
//        pathsBean.setDeviceId(pathsBean.getDeviceId());
        pathsBean.setUpdateTime( new Date().getTime());
//        pathsBean.setMapId(pathsBean.getMapId());
        int updateByPrimaryKey = pathsService.updateByPrimaryKey(pathsBean);
        if(updateByPrimaryKey>0){
//            System.out.println("updateByPrimaryKey: "+pathsBean);
            return Result.ok().msg("修改路径成功");
        }
        return Result.ok().msg("修改路径失败");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取所有清洁面积与清洁时长
     **/
    @PostMapping(value = "/clean_report/statistics")
    public Result getCleanAreaTotal(@RequestParam Map<String,Object> cleanReportMap) {
//        public Result getCleanAreaTotal(@RequestBody Map<String,Object> cleanReportMap) {
  /*{"customer_id":0}: */
        logger.info("获取清洁面积与清洁时长"+cleanReportMap);
        JSONObject commUtilJson = commUtil.getJson(cleanReportMap);
        List<Map<String, Object>> cleanReportsTotal = cleanReportService.selectCleanRportsTotal();
        if(cleanReportsTotal.size()==0){
            cleanReportsTotal=cleanReportService.selectCleanRportsTotalByCustomerId(Integer.parseInt(commUtilJson.get("customer_id").toString()));
        }
        Map<String,Object> map=new ConcurrentHashMap<>();
        map.put("top_clean_area",cleanReportsTotal.get(0).get("top_clean_area"));
        map.put("top_use_time",cleanReportsTotal.get(0).get("top_use_time"));
        return Result.ok().data("reports",map).msg("");
    }



    /**
     * @param
     * @param
     * @return Result
     * @description 获取指定设备下的清洁面积与清洁时长
     **/
    @GetMapping(value = "/clean_report/total")
    public Result getClearReportInfo(Integer device_id) {

        List<Map<String, Object>> maps = cleanReportService.selectCleanAreaAndUseTimeTotal(device_id);
        if(maps.size()<=0){
            return Result.error().msg("the deviceId clean_report is not exist!! ");
        }
        JSONObject jsonObject = JSONObject.parseObject(JSONArray.fromObject(maps).get(0).toString());
        List lst=new ArrayList();
        for (Map<String,Object> mp:maps) {
            Map<String,Object> cleanReportMap=new ConcurrentHashMap<>();
            cleanReportMap.put("update_time",mp.get("update_time"));
            cleanReportMap.put("clean_area",mp.get("clean_area"));
            cleanReportMap.put("use_time",mp.get("use_time"));
            lst.add(cleanReportMap);
        }
//        logger.info("获取指定设备下的清洁面积与清洁时长"+maps);
        Map<String,Object> map=new ConcurrentHashMap<>();
        map.put("count",maps.size());
        map.put("device_id",jsonObject.get("device_id"));
        map.put("clean_area_total",jsonObject.get("clean_area_total"));
        map.put("use_time_total",jsonObject.get("use_time_total"));
        map.put("clearReports",lst);
        return Result.ok().data(map).msg("");
    }




    /**
     * @param
     * @param
     * @return Result
     * @description 文件路径下载
     **/
    @GetMapping(value = "/data/download/report_path")
    public JSONArray getDownloadFile(Integer device_id,String file_name) {
//        logger.info("文件路径下载"+device_id+" : "+file_name);
        String pathList=Constants.Global.IMAGES_URL.getValue()+device_id+"/report_dir/"+file_name;
        ByteArrayOutputStream outputStream =null;
        InputStream inputStream =null;
        JSONArray jsonArray =null;
        try{
            URL url = new URL(pathList);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);// 设置是否要从 URL 连接读取数据,默认为true
            uc.connect();
             inputStream = uc.getInputStream();
            outputStream = new ByteArrayOutputStream(uc.getContentLength());
            byte[] temp = new byte[uc.getContentLength()];
            int size = 0;
            while ((size = inputStream.read(temp)) != -1) {
                outputStream.write(temp, 0, size);
            }
            if(outputStream==null){
                return JSONArray.fromObject("[]");
            }
            jsonArray = JSONArray.fromObject(outputStream.toString());
             outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
}
