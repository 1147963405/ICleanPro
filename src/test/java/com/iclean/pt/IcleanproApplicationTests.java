package com.iclean.pt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class IcleanproApplicationTests {

    @Autowired
    private DataSource dataSource;
    @Test
    void contextLoads() {
    }
    @Test
    void getConnection() throws SQLException {

        System.out.println(dataSource.getConnection());
    }
    /*@Test
    public void LiftTest() throws Exception {
        String accessKey = "other-test";
        String secrectKey = "qpzmqpzm";
        String method = "POST";
        long date = new Date().getTime();
        String signature = Sha1.encode(method+"|"+"/lift/service/order/apply_and_call|"+date+"|"+secrectKey);
        System.out.println(method+"|"+"/lift/service/order/apply_and_call|"+date+"|"+secrectKey);
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization",accessKey+":"+signature);
        System.out.println(accessKey+":"+signature);
        header.put("Time",date+"");
        System.out.println(date);
        header.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8; charset=utf-8");
        String data = "robotSn=12345&liftCode=1&srcFloor=2&destFloor=3&robotLoraAddr=1&sequence=1&CRC16=1722";
        String result = Http.post("http://112.95.225.155:8088/lift/service/order/apply_and_call", data, header);
        HttpResult httpResult = JSONObject.parseObject(result,HttpResult.class);
        Log.logger.error(httpResult.toString());
    }@Test
    public void LiftTest() throws Exception {
        String accessKey = "other-test";
        String secrectKey = "qpzmqpzm";
        String method = "POST";
        long date = new Date().getTime();
        String signature = Sha1.encode(method+"|"+"/lift/service/order/apply_and_call|"+date+"|"+secrectKey);
        System.out.println(method+"|"+"/lift/service/order/apply_and_call|"+date+"|"+secrectKey);
        Map<String,String> header = new HashMap<String,String>();
        header.put("Authorization",accessKey+":"+signature);
        System.out.println(accessKey+":"+signature);
        header.put("Time",date+"");
        System.out.println(date);
        header.put("Content-Type","application/x-www-form-urlencoded;charset=utf-8; charset=utf-8");
        String data = "robotSn=12345&liftCode=1&srcFloor=2&destFloor=3&robotLoraAddr=1&sequence=1&CRC16=1722";
        String result = Http.post("http://112.95.225.155:8088/lift/service/order/apply_and_call", data, header);
        HttpResult httpResult = JSONObject.parseObject(result,HttpResult.class);
        Log.logger.error(httpResult.toString());
    }*/
}
