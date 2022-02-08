package com.iclean.pt.sbgl.controller;

import com.alibaba.fastjson.JSONObject;
import com.iclean.pt.sbgl.bean.ModuleVersionBean;
import com.iclean.pt.sbgl.service.ModuleVersionService;
import com.iclean.pt.utils.Constants;
import com.iclean.pt.utils.FileToFtpUnits;
import com.iclean.pt.utils.Result;
import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class SystemController {


    @Autowired
    private ModuleVersionService moduleVersionService;

    /**
     * @param
     * @param
     * @return Result
     * @description 获取固件列表信息
     **/
    @RequestMapping(value = "/ota/version/moudle_info_list",method = RequestMethod.GET)
    public JSONObject getModuleInfoList(Integer type,Integer start_index,Integer count){

        /*type: 0
          count: 10
          start_index: 0*/
//        System.out.println("  type: "+type);
        /*通过type获取指定固件信息*/
        List  mvbs=new ArrayList<>();
        List<ModuleVersionBean> moduleVersionBeans = moduleVersionService.selectBySelective( type,start_index,count);
        if (moduleVersionBeans.size()<=0){
            return JSONObject.parseObject("{ successed: false, msg: '',errorCode: '',data: { m_v:[] , count: } }");
        }
        for (ModuleVersionBean moduleVersionBean:moduleVersionBeans) {
           Map<String,Object> map=new HashMap<>();
            map.put("ver_number",moduleVersionBean.getVerNumber());

             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String ut = simpleDateFormat.format(moduleVersionBean.getUpdateTime());
             String ct = simpleDateFormat.format(moduleVersionBean.getCreateTime());

            map.put("update_time",ut);
            map.put("create_time",ct);
            map.put("file_name",moduleVersionBean.getFileName());
            map.put("system_id",moduleVersionBean.getSystemId());
            map.put("description",moduleVersionBean.getDescription());
            map.put("support_type",moduleVersionBean.getSupportType());
            map.put("id",moduleVersionBean.getId());
            map.put("name",moduleVersionBean.getName());
            map.put("type",moduleVersionBean.getType());
            mvbs.add(map);
        }
        System.out.println("mvbs: "+mvbs);
        JSONArray jsonArray = new JSONArray().fromObject(mvbs);
        String str="{ successed: true, msg: '',errorCode: '',data: { m_v:"+jsonArray+", count:" +mvbs.size()+" } }";

        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject;
    }
    /**
     * @param
     * @param
     * @return Result
     * @description 新建固件
     **/
    @RequestMapping(value = "/version/update_module/v2",method = RequestMethod.POST)
    public Result addSystemModule(@RequestParam Map<String,Object> map, MultipartFile file) {

      /*name: test1222
       ver_number: v2.0
       description: test
       file: (binary)
       filename: map.png
       system_id: 0
       type: 0*/
      /* 新建固件map: {name=test122, ver_number=v2.0, description=2, filename=map.png, system_id=0, type=0}*/
        ModuleVersionBean moduleVersionBean=new ModuleVersionBean();
        moduleVersionBean.setName(String.valueOf(map.get("name")));
        moduleVersionBean.setVerNumber(Integer.parseInt(String.valueOf(map.get("ver_number"))));
        moduleVersionBean.setDescription(String.valueOf(map.get("description")));
        moduleVersionBean.setSystemId(Integer.parseInt(String.valueOf(map.get("system_id"))));
        moduleVersionBean.setType(Integer.parseInt(String.valueOf(map.get("type"))));
        moduleVersionBean.setCreateTime( new Date().getTime());
        moduleVersionBean.setUpdateTime( new Date().getTime());
        /*组装文件名*/
        String fileName=map.get("name")+".tar.gz";
        moduleVersionBean.setFileName(fileName);
        /*文件上传*/
        FileToFtpUnits ftp=new FileToFtpUnits(Constants.FtpServer.FTP_SERVER_IP.getValue(),
                Integer.parseInt(Constants.FtpServer.FTP_SERVER_PORT.getValue()),
                Constants.FtpServer.FTP_SERVER_USERNAME.getValue(),Constants.FtpServer.FTP_SERVER_PASWWORD.getValue());
        ftp.ftpLogin();
        ftp.uploadFile(file,fileName,Constants.Global.GLOBAL_OTA_FILE_DIR.getValue());
        ftp.ftpLogOut();
        int count = moduleVersionService.insert(moduleVersionBean);
        if(count==0){
            return Result.ok().msg("新建固件失败");
        }
        return Result.ok().msg("新建固件成功");
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 新建固件
     **/
    @RequestMapping(value = "/ota/version/system_info_list",method = RequestMethod.GET)
    public Result getSystemInfo(@RequestParam Map<String,Object> mp) {

        /*type: 0
          count: 10
          start_index: 0*/
        return null;
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 固件推送
     **/
    @RequestMapping(value = "/ota/version/start_upgrade",method = RequestMethod.GET)
    public Result startUpgrade(@RequestParam Map<String,Object> mp) {

        /*type: 0
          count: 10
          start_index: 0*/
        return null;
    }

    /**
     * @param
     * @param
     * @return Result
     * @description 删除固件
     **/
    @RequestMapping(value = "/ota/version/delete_moudle",method = RequestMethod.GET)
    public Result deleteModuleInfo(Integer id,Integer type) {
        /*id=53&type=0*/
        int count = moduleVersionService.deleteByPrimaryKey(id, type);
        if(count==0){
            return Result.ok().msg("删除固件成功");
        }
        return Result.ok().msg("删除固件失败");
    }
}
