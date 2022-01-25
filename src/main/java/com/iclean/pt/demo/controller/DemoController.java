/*
package com.iclean.pt.demo.controller;
import com.iclean.pt.common.config.Exception.GlobalException;
import com.iclean.pt.demo.bean.UserBean;
import com.iclean.pt.demo.service.UserService;
import com.iclean.pt.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
//接口模块描述信息
@Api(tags = "测试接口")
public class DemoController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list")
//    @ApiOperation(value="getList",tags={"获取用户信息copy"},notes="注意问题点")
    @ApiImplicitParam(name="id",value="用户id",dataType="String", paramType = "query")
    public Result getList(int id){
//        System.out.println("id为："+id);
        UserBean userBean = userService.selectByPrimaryKey(id);

        if(userBean.equals("")||userBean==null){
            throw  new GlobalException("查询用户不存在，请重新确认");
        }
        return Result.ok().data("user",userBean).msg("查询成功");
    }
}
*/
