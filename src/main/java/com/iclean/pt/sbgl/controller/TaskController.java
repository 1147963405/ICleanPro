package com.iclean.pt.sbgl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iclean.pt.sbgl.bean.TaskBean;
import com.iclean.pt.sbgl.service.TaskService;
import com.iclean.pt.utils.CommUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.service.CustomerService;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.parseInt;

@RestController
public class TaskController {

    private final static Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private CommUtil commUtil;
    @Autowired
    private CustomerService customerService;


    /**
     * @param
     * @param
     * @return Result
     * @description 新增任务
     **/
    @PostMapping(value = "/task/add")
    public Result addTask(@RequestParam Map<String,Object> map) {

        /*步骤
         * 1.获取map
         * 2.解析map,获取map中的数据
         * 3.组装taskbean实例对象
         * */
        JSONObject jsonObj = commUtil.getJson(map);
        /*2、3*/
        TaskBean taskBean=new TaskBean();
        taskBean.setDeviceId(parseInt(String.valueOf(jsonObj.get("device_id"))));
        taskBean.setLoopCount(parseInt(String.valueOf(jsonObj.get("loop_count"))));
        taskBean.setMapId(parseInt(String.valueOf(jsonObj.get("map_id"))));
        taskBean.setName(String.valueOf(jsonObj.get("name")));
        taskBean.setParameter(String.valueOf(jsonObj.get("parameter")));
        taskBean.setType(parseInt(String.valueOf(jsonObj.get("type"))));
        taskBean.setWorkDay(parseInt(String.valueOf(jsonObj.get("work_day"))));
        taskBean.setWorkParts(String.valueOf(jsonObj.get("work_parts")));
        taskBean.setWorkTime(parseInt(String.valueOf(jsonObj.get("work_time"))));
        taskBean.setWorkType(parseInt(String.valueOf(jsonObj.get("work_type"))));
        taskBean.setUpdateTime( new Date().getTime());

        int insert = taskService.insert(taskBean);
        if(insert<=0){
            return Result.ok().msg("新增任务失败");
        }
        return Result.ok().msg("新增任务成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 更新任务
     **/
    @PostMapping(value = "/task/update")
    public Result updateTask(@RequestParam Map<String,Object> map) {
        /*步骤
         * 1.获取map并解析map中的数据
         * 2.通过任务id获取任务对象
         * 3.对任务对象进行更新
         * */
        /*1*/
        JSONObject jsonObj = commUtil.getJson(map);
        TaskBean taskBean = taskService.selectByPrimaryKey(parseInt(String.valueOf(jsonObj.get("id"))));
        taskBean.setLoopCount(parseInt(String.valueOf(jsonObj.get("loop_count"))));
        taskBean.setName(String.valueOf(jsonObj.get("name")));
        taskBean.setParameter(String.valueOf(jsonObj.get("parameter")));
        taskBean.setType(parseInt(String.valueOf(jsonObj.get("type"))));
        taskBean.setWorkDay(parseInt(String.valueOf(jsonObj.get("work_day"))));
        taskBean.setWorkParts(String.valueOf(jsonObj.get("work_parts")));
        taskBean.setWorkTime(parseInt(String.valueOf(jsonObj.get("work_time"))));
        taskBean.setWorkType(parseInt(String.valueOf(jsonObj.get("work_type"))));
        taskBean.setUpdateTime(  new Date().getTime());

        int count = taskService.updateByPrimaryKey(taskBean);
        if(count<=0){
            return Result.ok().msg("更新任务失败");
        }
        return Result.ok().msg("更新任务成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 删除任务
     **/
    @GetMapping(value = "/task/delete")
    public Result deleteTask(@RequestParam Map<String,Object> map) {
/*id: 1221
map_name: 2
device_id: 182
name: 20211216t*/

        int count = taskService.deleteByPrimaryKey(parseInt(String.valueOf(map.get("id"))));
        if(count>0){
            return Result.ok().msg("删除任务失败");
        }
        return Result.ok().msg("删除任务成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取任务列表
     **/
    @RequestMapping(value = "/task/lists", method = RequestMethod.POST)
//    public Result getTaskList(@RequestBody Map<String,Object> map){
    public Result getTaskList(@RequestParam Map<String,Object> map){
        logger.info("获取任务列表:"+map);
        /*{"user_id":1,"start_index":0,"count":10}:
         * 条件筛选：{"user_id":1,"start_index":0,"count":10,"device_type_id":1}: 设备类型 device_type_id
           {"user_id":1,"start_index":0,"count":10,"device_status":"1"}:  设备状态 device_status
            {"user_id":1,"start_index":0,"count":10,"task_mode":"0"}:   任务类型work_type
             {"user_id":1,"start_index":0,"count":10,"device_name":"长桥街"}:
             {"user_id":1,"device_id":128,"start_index":0,"count":9999}: */
        /*步骤
         * 1通过user获取到指定的客户
         * 2如果客户存在则通过该客户查询名下的所有设备，以及设备下的所有任务
         * 3.否则查询所有客户及客户名下的所有设备，以及设备下的任务
         *
         * */

        JSONObject jsonObj = commUtil.getJson(map);
        int startIndex = parseInt(String.valueOf(jsonObj.get("start_index")));
        int count = parseInt(String.valueOf(jsonObj.get("count")));
        int userId = parseInt(String.valueOf(jsonObj.get("user_id")));
        List<Map<String, Object>> tasksList =null;
        int counts = customerService.selectCustomer(userId);
        if(counts==0){
            if(jsonObj.get("device_name")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and  t.name like '%"+jsonObj.get("device_name")+"%'");
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksBySelective(deviceNameSql.toString());
            }else if(jsonObj.get("device_type_id")!=null){
                StringBuilder typeIdSql = new StringBuilder();
                typeIdSql.append(" and  t.type_id="+jsonObj.get("device_type_id"));
//                String typeId=" t.type_id="+jsonObj.get("device_type_id");
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksBySelective(typeIdSql.toString());
            }else if(jsonObj.get("device_status")!=null){
                StringBuilder statusSql = new StringBuilder();
                statusSql.append(" and  t.status="+jsonObj.get("device_status"));
//                String status=" t.status="+jsonObj.get("device_status");
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksBySelective(statusSql.toString());

            }else if(jsonObj.get("task_mode")!=null){
                StringBuilder andSql = new StringBuilder();
                andSql.append(" and  r.work_type="+jsonObj.get("task_mode"));
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksBySelective(andSql.toString());

            }else if(jsonObj.get("device_id")!=null){
                StringBuilder deviceSql = new StringBuilder();
                deviceSql.append(" and  r.device_id="+jsonObj.get("device_id"));
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksBySelective(deviceSql.toString());
            }else {
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasks();
            }
        }else {
            if(jsonObj.get("device_name")!=null){
                StringBuilder deviceNameSql = new StringBuilder();
                deviceNameSql.append(" and  t.name like '%"+jsonObj.get("device_name")+"%'");
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksByParams(userId,deviceNameSql.toString());
            }else if(jsonObj.get("device_type_id")!=null){
                StringBuilder typeIdSql = new StringBuilder();
                typeIdSql.append(" and  t.type_id="+jsonObj.get("device_type_id"));
//                String typeId=" t.type_id="+jsonObj.get("device_type_id");
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksByParams(userId,typeIdSql.toString());
            }else if(jsonObj.get("device_status")!=null){
                StringBuilder statusSql = new StringBuilder();
                statusSql.append(" and  t.status="+jsonObj.get("device_status"));
//                String status=" t.status="+jsonObj.get("device_status");
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksByParams(userId,statusSql.toString());

            }else if(jsonObj.get("task_mode")!=null){
                StringBuilder workTypeSql = new StringBuilder();
                workTypeSql.append(" and  r.work_type="+jsonObj.get("task_mode"));
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksByParams(userId,workTypeSql.toString());

            }else if(jsonObj.get("device_id")!=null){
                StringBuilder deviceSql = new StringBuilder();
                deviceSql.append(" and  r.device_id="+jsonObj.get("device_id"));
                PageHelper.offsetPage(startIndex, count);
                tasksList = taskService.selectTasksByParams(userId,deviceSql.toString());
            }else {
                PageHelper.offsetPage(startIndex, count);
                tasksList =taskService.selectTasksByUserId(userId);
            }
        }
        long total = ((com.github.pagehelper.Page) tasksList).getTotal();

        Map<String,Object> jsonMap=new ConcurrentHashMap<>();
        jsonMap.put("count",total);
        jsonMap.put("tasks",tasksList);
        return Result.ok().data(jsonMap).msg("");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取今天的任务信息
     **/
    @PostMapping(value = "/task/today_task_info")
    public Result  getTodayTaskInfo(Integer user_id) {

        /*步骤：
         * 1.通过用户id获取到指定客户，然后通过客户id获取到指定客户下的所有设备，并通过设备id获取到指定设备名下的所有任务
         * 2.如果用户id对应的客户id不存在,则查询所有客户信息
         * 3.
         * */

        return null;
    }
}
