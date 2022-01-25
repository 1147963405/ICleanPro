package com.iclean.pt.yhgl.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.iclean.pt.utils.CommnosResult;
import com.iclean.pt.utils.RedisUtil;
import com.iclean.pt.utils.Result;
import com.iclean.pt.yhgl.bean.*;
import com.iclean.pt.yhgl.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;



@RestController
public class UserController {

    /*配置共享JSON格式转换*/
     static SerializeConfig config=new SerializeConfig() ;

    @Autowired
    private UserService userService;
    @Autowired
    private UserTypeService userTypeService;
    @Autowired
    private UserAuthorityService userAuthorityService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerGroupService customerGroupService;
    @Autowired
    private CustomerDeviceService customerDeviceService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DeviceService deviceService;
     @Autowired
     private  RedisUtil redisUtil;



    /**
     * @param name
     * @param password
     * @return Result
     * @description 用户登录
     **/
    @GetMapping(value = "/users/Login")
    public Result selectOne(@Param("name") String name, @Param("password")String password) {
        /*
         * 步骤：
         * 1.通过账号密码查询用户是否存在(查用户表)
         * 2.如果用户存在，则对该用户进行用户类型及权限验证(查用户类型表和用户验证表，通过id等外键关联)
         * 3.如果验证成功，则登录成功，反之失败
         * */
        UserBean userBean = userService.selectBySelective(name, password);
        Map<String, Object> hp = new HashMap<>();
        if (userBean != null) {
            /*通过用户类型id获取用户类型信息*/
            UserTypeBean userTypeBean = userTypeService.selectByPrimaryKey(userBean.getTypeId());
            if (userTypeBean != null) {
                /*通过用户类型id获取制定用户验证信息*/
                UserAuthorityBean userAuthorityBean = userAuthorityService.selectByUserTypeId(userTypeBean.getId());
                UserAuthorityCopyBean userAuthorityCopyBean = BeanUtil.copyProperties(userAuthorityBean, UserAuthorityCopyBean.class);
                hp.put("id", userBean.getId());
                hp.put("name", userBean.getName());
                hp.put("token", "token");
                hp.put("level", userTypeBean.getLevel());
                hp.put("auth", userAuthorityCopyBean);
                hp.put("customerId", userBean.getCustomerId());
                config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                String json = JSON.toJSONString(hp, config);
                JSONObject jsonMap = JSONObject.parseObject(json);//json转map
                /*通过redis缓存登录的用户信息 */
                redisUtil.hset("users", String.valueOf(userBean.getId()),userBean);
                redisUtil.expire(String.valueOf(userBean.getId()),1800);//30分钟过期
                return Result.ok().data(jsonMap).msg("用户登录成功");

            } else {
                /*该用户为普通用户*/
                return Result.error().msg("该用户为普通用户");
            }
        }
        return Result.error().msg("账号或密码错误,请重新输入");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 获取用户类型
     **/
    @GetMapping(value = "/users/get_user_type")
    public CommnosResult getUserType() {
        CommnosResult cr=new CommnosResult();
        List<UserTypeBean> userTypeBeans = userTypeService.selectList();
        if (userTypeBeans.size() < 0) {
            cr.setMsg("用户类型不存在");
            cr.setCode(202);
            return cr;
        }
        cr.setMsg("获取用户类型成功");
        cr.setCode(200);
        cr.setData(userTypeBeans);
        return cr;
    }

    /**
     * @param record
     * @return Result
     * @description 添加用户类型
     **/
    @PostMapping(value = "/users/add_user_type")
    public Result addUserType(@RequestParam Map<String,Object>  record) {

        String key ="";
        Iterator<String> it = record.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> userTypeMap = JSONObject.parseObject(key);
        UserTypeBean userTypeBean = new UserTypeBean();
        userTypeBean.setLevel((Integer) userTypeMap.get("level"));
        userTypeBean.setName((String) userTypeMap.get("name"));
        int count = userTypeService.insertSelective(userTypeBean);
        if (count < 0) {
            return Result.error().msg("添加用户类型失败");
        }
        return Result.ok().msg("添加用户类型成功");
    }

    /**
     * @param record
     * @return Result
     * @description 更新用户类型
     **/
    @PostMapping(value = "/users/update_user_type")
    public Result updateUserType(@RequestParam Map<String,Object>  record) {
        /*
         * 步骤：
         *  1.获取用户类型集合
         *  2.比对用户类型集合中需要修改的用户类型id
         *  3.进行该用户类型的修改
         * */
        String key ="";
        Iterator<String> it = record.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> userTypeMap = JSONObject.parseObject(key);
        UserTypeBean userTypeBean = userTypeService.selectByPrimaryKey((Integer) userTypeMap.get("id"));
        userTypeBean.setLevel((Integer) userTypeMap.get("level"));
        userTypeBean.setName((String) userTypeMap.get("name"));
        int count = userTypeService.updateByPrimaryKeySelective(userTypeBean);

        if (count < 0) {
            return Result.error().msg("更新用户类型失败");
        }
        return Result.ok().msg("更新用户类型成功");
    }

    /**
     * @param id
     * @return Result
     * @description 删除用户类型
     **/
    @GetMapping(value = "/users/delete_user_type")
    public Result deleteUserType(int id) {

        int count = userTypeService.deleteByPrimaryKey(id);

        if (count < 0) {
            return Result.error().msg("删除用户类型失败");
        }

        return Result.ok().data("userTypeBean", count).msg("删除用户类型成功");
    }


    /**
     * @param id
     * @param
     * @return Result
     * @description 获取用户权限
     **/
    @GetMapping(value = "authority/get")
    public Result getUserAuth(int id) {
        UserAuthorityBean userAuthorityBean = userAuthorityService.selectByUserTypeId(id);
        if (userAuthorityBean == null) {
            return Result.error().msg("用户权限id不存在");
        }
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            String jsonString = JSON.toJSONString(userAuthorityBean, config);
            JSONObject jsonMap = JSONObject.parseObject(jsonString);//json转map
        return Result.ok().data(jsonMap).msg("获取用户权限成功");
    }

    /**
     * @param record
     * @return Result
     * @description 修改用户权限
     **/
    @PostMapping(value = "/authority/update")
    public Result updateUserAuth(@RequestParam Map<String,Object>  record) {

        String key ="";
        Iterator<String> it = record.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> userAuthMap = JSONObject.parseObject(key);
        UserAuthorityBean authorityBean = userAuthorityService.selectByPrimaryKey((Integer) userAuthMap.get("id"));
        authorityBean.setUserTypeId((Integer) userAuthMap.get("user_type_id"));
        authorityBean.setMonitorAuth((Integer) userAuthMap.get("monitor_auth"));
        authorityBean.setCustomerMangerAuth((Integer) userAuthMap.get("customer_manger_auth"));
        authorityBean.setDeviceMangerAuth((Integer) userAuthMap.get("device_manger_auth"));
        authorityBean.setMapMangerAuth((Integer) userAuthMap.get("map_manger_auth"));
        authorityBean.setTaskMangerAuth((Integer) userAuthMap.get("task_manger_auth"));
        authorityBean.setVideoMangerAuth((Integer) userAuthMap.get("video_manger_auth"));
        authorityBean.setReportDataAuth((Integer) userAuthMap.get("report_data_auth"));
        authorityBean.setUpgradeAuth((Integer) userAuthMap.get("upgrade_auth"));
        authorityBean.setOrderAuth((Integer) userAuthMap.get("order_auth"));
        authorityBean.setGpsDataAuth((Integer) userAuthMap.get("gps_data_auth"));

        int count = userAuthorityService.updateByPrimaryKeySelective(authorityBean);
        if (count < 0) {
            return Result.error().msg("修改用户权限失败");
        }

        return Result.ok().msg("修改用户权限成功");
    }


    /**
     * @param
     * @param
     * @return Result
     * @description 获取客户组
     **/
    @PostMapping(value = "/customer_group/list")
    public Result getCustomerGroup(@RequestParam Map<String, Object> params) {

            String key ="";
                Iterator<String> it = params.keySet().iterator(); //map.keySet()得到的是set集合，可以使用迭代器遍历
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> jsm = JSONObject.parseObject(key);
        Integer user_id = (Integer) jsm.get("user_id");
        Integer id = (Integer) jsm.get("id");
        /*
         * 步骤：
         * 1.通过用户user_id获取登录用户信息
         * 2.通过用户信息中customer_id获取匹配的指定客户
         * 3.通过客户表中customer_group_id获取同一客户组下的客户列表
         *   客户和客户组存在以下几种可能:
         *                               1)客户id与客户组id都存在
         *                               2)客户对应ID不存在，客户组id存在
         *                              3)客户对应id存在，客户组id不存在
         * */
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        /*1.通过用户user_id获取登录用户信息*/
        UserBean userBean = userService.selectByPrimaryKey( user_id);
        Map<String, Object> rParam = new HashMap<>();
        List<CustomerGroupBean> customerGroupBeans =customerGroupService.selectByPrimaryKey( id);
        /*1-1.账号为空时，使用客户组表中parent_id获取客户相关信息*/
        if(userBean==null){
                rParam.put("customer",customerService.selectBySelective(id,null));
//                System.out.println("customerService:"+customerService.selectBySelective(id));
                rParam.put("customerGroup", customerGroupBeans);
            /*通过JSON转换为下划线格式参数*/
            String toJSON = JSON.toJSONString(rParam, config);
            JSONObject jsonMap = JSONObject.parseObject(toJSON);//json转map
                return Result.ok().data(jsonMap).msg("");
        }
        /*2.通过用户信息中customer_id获取匹配指定客户*/
        CustomerBean customerBean = customerService.selectByPrimaryKey(userBean.getCustomerId());
        /*2-1.账号不为空时，对应的客户customer_id获取的客户为空时，使用客户组表中parent_id获取客户相关信息*/
        if(customerBean==null){
//            System.out.println(" 账号不为空时，对应的客户customer_id获取的客户为空时");
            rParam.put("customer",customerService.selectBySelective( id,null));
            rParam.put("customerGroup", customerGroupBeans);
            String Jstr = JSON.toJSONString(rParam, config);
            JSONObject jMap = JSONObject.parseObject(Jstr);//json转map
            return Result.ok().data(jMap).msg("");
        }
        /*3.通过客户表中customer_group_id获取同一客户组下的客户列表*/
        List<CustomerBean> customerBeans = customerService.selectBySelective(customerBean.getCustomerGroupId(),null);
        for (CustomerBean cb:customerBeans) {
            for (CustomerGroupBean cgb:customerGroupBeans) {
                /*通过客户组id判断客户组表parent_id中是否存在该客户组*/
                if(cb.getCustomerGroupId().equals(cgb.getParentId())){
//                    System.out.println("getCustomerGroupId:    "+cb.getCustomerGroupId()+"getParentId：  "+cgb.getParentId());
                    rParam.put("customer", customerBeans);
                    rParam.put("customerGroup", customerGroupBeans);
                    String toJSON = JSON.toJSONString(rParam, config);
                    JSONObject jsonMap = JSONObject.parseObject(toJSON);//json转map
                    return Result.ok().data(jsonMap).msg("获取客户客户组成功");
                }
                /*客户组中parent_id与客户中的customer_group_id不相等*/
                rParam.put("customer", customerBeans);
                rParam.put("customerGroup", customerGroupBeans);
                String toJSON = JSON.toJSONString(rParam, config);
                JSONObject jsonMap = JSONObject.parseObject(toJSON);//json转map
                return Result.ok().data(jsonMap).msg("");
            }
        }
        return Result.error().msg("");
    }


    /**
     * @param record
     * @return Result
     * @description 新增客户组
     **/
    @PostMapping(value = "/customer_group/add")
    public Result addCustomerGroup(@RequestParam Map<String,Object>  record) {
    /*{"name":"测试","parent_id":0,"description":"新建分组"}: */
        String key ="";
        Iterator<String> it = record.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> customerGroupMap = JSONObject.parseObject(key);
        CustomerGroupBean cgb=new CustomerGroupBean();
        cgb.setDescription((String) customerGroupMap.get("description"));
        cgb.setParentId((Integer) customerGroupMap.get("parent_id"));
        cgb.setName((String) customerGroupMap.get("name"));

        int count = customerGroupService.insertSelective(cgb);
        if (count < 0) {

            return Result.error().msg("新增客户组失败");
        }

        return Result.ok().msg("新增客户组成功");
    }

    /**
     * @description  更新客户组
     * @param record
     * @return Result
     **/
    @PostMapping(value = "/customer_group/update")
    public Result updateCustomerGroup(@RequestParam Map<String,Object>  record){

        String key ="";
        Iterator<String> it = record.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> customerGroupMap = JSONObject.parseObject(key);

        CustomerGroupBean groupBeans = customerGroupService.queryByPrimaryKey((Integer) customerGroupMap.get("id"));
        groupBeans.setDescription((String) customerGroupMap.get("description"));
        groupBeans.setParentId((Integer) customerGroupMap.get("parent_id"));
        groupBeans.setName((String) customerGroupMap.get("name"));
        int count = customerGroupService.updateByPrimaryKeySelective(groupBeans);
        if(count<0){

            return Result.error().msg("更新客户组失败");
        }

        return Result.ok().msg("更新客户组成功");

    }

    /**
     * @param id
     * @return Result
     * @description 删除客户组
     **/
    @GetMapping(value = "/customer_group/delete")
    public Result deleteCustomerGroup(int id) {

        int count = customerGroupService.deleteByPrimaryKey(id);

        if(count<0){
            return Result.error().msg("删除客户组失败");
        }
        return Result.ok().data("customerGroupBean",count).msg("删除客户组成功");

    }

    /**
     * @param addCustomer
     * @return Result
     * @description 增加客户
     **/
    @PostMapping(value = "/customer/add")
    public Result addCustomer(@RequestParam Map<String,Object> addCustomer) {
        /*{"customer_group_id":4,"name":"石楼","email":"1@qq.com",
        "address":"石楼","contact_person":"石楼","leader_person":"石楼",
        "phone":"15298221532","description":"石楼"}: */
        String key ="";
        Iterator<String> it = addCustomer.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> customerMap = JSONObject.parseObject(key);
      CustomerBean cb=new CustomerBean();
        /*update_time通过new Date获取*/
        //当前时间
        Date date = DateUtil.date();
        long time = date.getTime();
        cb.setUpdateTime(time);
        cb.setAddress((String) customerMap.get("address"));
        cb.setCustomerGroupId((Integer) customerMap.get("customer_group_id"));
        cb.setName((String) customerMap.get("name"));
        cb.setEmail((String) customerMap.get("email"));
        cb.setContactPerson((String) customerMap.get("contact_person"));
        cb.setLeaderPerson((String) customerMap.get("leader_person"));
        cb.setPhone((String) customerMap.get("phone"));
        cb.setDescription((String) customerMap.get("description"));
        int count = customerService.insertSelective(cb);
        if(count<0){

            return Result.error().msg("增加客户失败");
        }

        return Result.ok().msg("增加客户成功");
    }

    /**
     * @param id
     * @return Result
     * @description 删除客户
     **/
    @GetMapping(value = "/customer/delete")
    public Result deleteCustomer(int id) {

        int count = customerService.deleteByPrimaryKey(id);
        if(count<0){

            return Result.error().msg("删除客户失败");
        }
        return Result.ok().data("customerBean",count).msg("删除客户成功");
    }

    /**
     * @param params
     * @return Result
     * @description 获取账户列表
     **/
    @PostMapping(value = "/users/lists")
    public Result getUserList(@RequestParam Map<String, Object> params) {
//        System.out.println("params: "+params);
      /*获取map中的参数customer_id、type_id、start_index、count*/
        String key ="";
        Iterator<String> it = params.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> jsm = JSONObject.parseObject(key);
        Integer customer_id = (Integer) jsm.get("customer_id");
        Integer type_id = (Integer) jsm.get("type_id");
        Integer start_index = (Integer) jsm.get("start_index");
        Integer count = (Integer) jsm.get("count");
/*
* 步骤：
* 1.点击用户模块，{"customer_id":0,"start_index":0,"count":10}即获取登录用户自带的customer_id
* 2.分组模块：点击某一客户，获取到customer_id，通过该ID遍历出用户表中指定的客户列表
* 3.角色模块：点击角色模块，获取所有用户，分页
* 4.点击某一角色，通过type_id获取到与之匹配的所有角色 分页，customer_id不变
* 5.type_id和customer_id存在几种关系：1)角色模块：customer_id不变，根据type_id获取对应客户列表
*                                     2)分组模块：type_id不变或者不传，根据customer_id获取对应客户列表
* */

/*分组模块：1.通过customer_id从tb_user表中获取到与之匹配的所用用户
*           2.对获取到的用户列表分页 limit
*角色模块：1.通过type_id从tb_user表中获取与之匹配的所有用户
*          2.对获取的用户列表分页 limit
*  */
        List<UserBean> userBeans = userService.queryByCustomerIdOrTypeId(customer_id, type_id);
        List<UserBean> usersPage = userService.queryByPage(customer_id, type_id, start_index, count);
        Map<String,Object> hp=new HashMap<>();
        hp.put("users",usersPage);
        hp.put("count",userBeans.size());
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String jStr = JSON.toJSONString(hp, config);
        JSONObject jsonMap = JSONObject.parseObject(jStr);//json转map
        return Result.ok().data(jsonMap).msg("获取账户列表成功");
    }

    /**
     * @description  更新客户
     * @param updateCustomer
     * @return Result
     **/
    @PostMapping(value = "/customer/update")
    public Result updateCustomer(@RequestParam Map<String,Object> updateCustomer){

        String key ="";
        Iterator<String> it = updateCustomer.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> customerMap = JSONObject.parseObject(key);
        CustomerBean cb = customerService.selectByPrimaryKey((Integer) customerMap.get("id"));
        cb.setAddress((String) customerMap.get("address"));
        cb.setCustomerGroupId((Integer) customerMap.get("customer_group_id"));
        cb.setName((String) customerMap.get("name"));
        cb.setEmail((String) customerMap.get("email"));
        cb.setContactPerson((String) customerMap.get("contact_person"));
        cb.setLeaderPerson((String) customerMap.get("leader_person"));
        cb.setPhone((String) customerMap.get("phone"));
        cb.setDescription((String) customerMap.get("description"));

        int count = customerService.updateByPrimaryKeySelective(cb);
        if(count<0){

            return Result.error().msg("更新客户失败");
        }

        return Result.ok().msg("更新客户成功");
    }

    /**
     * @param map
     * @return Result
     * @description 新增账户
     **/
    @PostMapping(value = "/users/add_user")
    public Result addUser(@RequestParam Map<String,Object> map) {
//        System.out.println("map:     "+map);
        UserBean userBean=new UserBean();
        String key ="";
        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }

        Map<String,Object> jsm = JSONObject.parseObject(key);
//        System.out.println("jsm:     "+jsm);
        /*update_time通过new Date获取*/
        //当前时间
        Date date = DateUtil.date();
        long time = date.getTime();
        userBean.setUpdateTime(time);
        userBean.setName((String) jsm.get("name"));
        userBean.setUserName((String) jsm.get("user_name"));
        userBean.setPasswd((String) jsm.get("passwd"));
        userBean.setContactPerson((String) jsm.get("contact_person"));
        userBean.setEmail((String) jsm.get("email"));
        userBean.setTypeId((Integer) jsm.get("type_id"));
        userBean.setStatus((Integer) jsm.get("status"));
        userBean.setDescription((String) jsm.get("description"));
        userBean.setPhone((String) jsm.get("phone"));
        userBean.setCustomerId((Integer) jsm.get("customer_id"));

        int count = userService.insertSelective(userBean);
        if(count<0){

            return Result.error().msg("新增账户失败");
        }
        return Result.ok().msg("新增账户成功");
    }

    /**
     * @description  更新账户
     * @param params
     * @return Result
     **/
    @PostMapping(value = "/users/update_user")
    public Result updateUser(@RequestParam Map<String,Object> params){
    /*{"id":57,"status":1}: */
        /*
        * 步骤：1.通过id获取userbean
        *       2.更新对应的参数值
        * */
        String key ="";
        Iterator<String> it = params.keySet().iterator();
        while(it.hasNext()){
            key = it.next();
        }
        Map<String,Object> jsm = JSONObject.parseObject(key);
        UserBean userBean = userService.selectByPrimaryKey((Integer) jsm.get("id"));
        userBean.setName((String) jsm.get("name"));
        userBean.setUserName((String) jsm.get("user_name"));
        userBean.setPasswd((String) jsm.get("passwd"));
        userBean.setContactPerson((String) jsm.get("contact_person"));
        userBean.setEmail((String) jsm.get("email"));
        userBean.setTypeId((Integer) jsm.get("type_id"));
        userBean.setStatus((Integer) jsm.get("status"));
        userBean.setDescription((String) jsm.get("description"));
        userBean.setPhone((String) jsm.get("phone"));
        userBean.setCustomerId((Integer) jsm.get("customer_id"));
        int count = userService.updateByPrimaryKeySelective(userBean);
        if(count<0){

            return Result.error().msg("更新账户失败");
        }
        return Result.ok().msg("更新账户成功");
    }

    /**
     * @param id
     * @return Result
     * @description 删除账户
     **/
    @GetMapping(value = "/users/delete_user")
    public Result deleteUser(int id) {

        int count = userService.deleteByPrimaryKey(id);
        if(count<0){

            return Result.error().msg("删除账户失败");
        }
        return Result.ok().data("userBean",count).msg("删除账户成功");
    }

    /**
     * @param id
     * @return Result
     * @description 获取客户详情
     **/
    @GetMapping(value = "/customer/get")
    public Result getUserList(int id) {

        /*
        * 步骤：
        * 1.通过中间表获取到客户信息、设备信息,客户与设备关系是一对多关系：即customer--list<device>
        * 2.通过订单表中的客户id关联对应的客户，客户与订单是一对多关联：即customer--list<order>
        * 3.获取到客户详情信息后，通过一个map封装
        * */

//        System.out.println("customerDeviceBeans:"+customerDeviceBeans);
        Map<String,Object>  customer=new HashMap<>();
        Map<String,Object>  device=new HashMap<>();
        Map<String,Object>  order=new HashMap<>();
        List lstD=new ArrayList();
        List lstO=new ArrayList();
        /*获取DeviceInfoBean类中指定参数*/
        List<DeviceInfoBean> deviceInfoBeans =new ArrayList<>();
        DeviceInfoBean deviceInfoBean =null;
        /*1.获取中间表信息*/
        List<CustomerDeviceBean> customerDeviceBeans = customerDeviceService.selectCustomerWithDevices(id);
        /*1-1.遍历中间表提取出设备信息列表*/
        for (int i=0;i<customerDeviceBeans.size();i++){
            /*1-2.获取设备列表*/
            deviceInfoBean = deviceService.selectByPrimaryKey(customerDeviceBeans.get(i).getDeviceId());
            deviceInfoBeans.add(deviceInfoBean);
        }

        List<DeviceInfoCopyBean> deviceCopyBeans=new ArrayList<>();
        for (int i=0;i<deviceInfoBeans.size();i++) {
             deviceInfoBean = deviceInfoBeans.get(i);
            DeviceInfoCopyBean deviceInfoCopyBean=new DeviceInfoCopyBean();
            deviceInfoCopyBean.setName(deviceInfoBean.getName());
            deviceInfoCopyBean.setId(deviceInfoBean.getId());
            deviceCopyBeans.add(deviceInfoCopyBean);
        }
//        System.out.println("deviceInfoBeans:"+deviceInfoBeans.toString());
        /*1-3.通过客户id获取客户信息*/
        CustomerBean customerBean = customerService.selectByPrimaryKey(id);
//        System.out.println("customerBean:"+customerBean);
        /*2.通过客户id获取客户的订单列表*/
        List<OrderBean> orderBeans = orderService.selectBySelective(id);
        /*获取订单指定参数*/
        List<OrderCopyBean> orderCopyBeans =new ArrayList<>();
        for(int i=0;i<orderBeans.size();i++){

            OrderBean orderBean = orderBeans.get(i);
            OrderCopyBean copyBean=new OrderCopyBean();
            copyBean.setId(orderBean.getId());
            orderCopyBeans.add(copyBean);
        }
        /*通过map再封装一层*/
        device.put("devices",deviceCopyBeans);
        device.put("size",deviceCopyBeans.size());
        order.put("orders",orderCopyBeans);
        order.put("size",orderCopyBeans.size());
        lstD.add(device);
        lstO.add(order);
        /*3.组装客户详情信息*/
        /*当客户customer_id不存在时*/
        if(customerDeviceBeans.size()<=0&&customerBean==null&&orderBeans.size()<=0){
            customer.put("customer", "[]");
            return Result.error().msg("该客户id不存在");
        }else {
            CustomerWithDevicesAndOrders cdo = BeanUtil.copyProperties(customerBean, CustomerWithDevicesAndOrders.class);
            cdo.setAbort_device(lstD);
            cdo.setAbort_order(lstO);

            /*通过JSON转换为下划线格式参数*/
            Map<String,Object> mp=new HashMap<>();
            mp.put("customer",cdo);
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            String jStr = JSON.toJSONString(mp, config);
            JSONObject jsonMap = JSONObject.parseObject(jStr);//json转map
            return Result.ok().data(jsonMap).msg("");
        }
    }

    /**
     * @param user_id
     * @return CommnosResult
     * @description 获取用户详情
     **/
    @GetMapping(value = "/user")
    public Result getUser(int user_id) {

        UserBean userBean = userService.selectByPrimaryKey(user_id);
        if(userBean==null){
            return Result.error().msg("该用户user_id不存在");
        }
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String jStr = JSON.toJSONString(userBean, config);
        JSONObject jsonMap = JSONObject.parseObject(jStr);//json转map
        return Result.ok().data(jsonMap).msg("获取用户详情成功");
    }
}
