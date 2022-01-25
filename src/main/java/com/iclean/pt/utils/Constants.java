package com.iclean.pt.utils;



public class Constants {

    /*Mqtt消息队列主题发布订阅*/
    public static enum Mqtt{


//        SERVER_URL("SERVER_URL","tcp://192.168.11.76:1883"),
        SERVER_URL("SERVER_URL","tcp://47.92.192.154:1883"),
        SERVER_USERNAME("SERVER_USERNAME","admin"),
        SERVER_PASSWORD("SERVER_PASSWORD","password"),
         /*Publish 主题发布*/
         PRODUCER_CLIENTID("PRODUCER_CLIENTID","mqttProducer"),
        TOPIC_PUB_ROBOT_MSG("TOPIC_PUB_ROBOT_MSG","iclean/cloud/message"),
        TOPIC_PUB_ROBOT_UPGRADE_MSG("TOPIC_PUB_ROBOT_UPGRADE_MSG","iclean/cloud/upgrade"),
        TOPIC_PUB_ROBOT_VEDIO_MSG("TOPIC_PUB_ROBOT_VEDIO_MSG","iclean/cloud/vedio"),

        /*Subscribe  订阅主题*/
        CONSUMER_CLIENTID("CONSUMER_CLIENTID","mqttConsumer"),//
        //机器的状态信息，如上线，下线，空闲，作务，急停等状态变化时
        TOPIC_SUB_ROBOT_STATUS("TOPIC_SUB_ROBOT_STATUS","iclean/robot/status"),
        //普通的上传信息，如传感器，位置，任务信息上报
        TOPIC_SUB_ROBOT_MSG("TOPIC_SUB_ROBOT_MSG","iclean/robot/message/#"),
        //重要的操作信息，如地图，任务，分区等信息编辑
        TOPIC_SUB_ROBOT_MSGEX("TOPIC_SUB_ROBOT_MSGEX","iclean/robot/messageEx/#");

         Mqtt(String name,String value){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /*Jedis 连接配置*/
    public static enum Jedis{
        SERVER_HOST("SERVER_HOST","127.0.0.1"),
        SERVER_PORT("SERVER_PORT","6379");

         Jedis(String name,String value){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }


    /*const std::string GLOBAL_MAP_PATH = "data/map";

const std::string GLOBAL_LOGO_DIR = "data/logo";

const std::string GLOBAL_HELPFILE_DIR = "data/helpfile";

const std::string GLOBAL_VEDIO_PROGRAM_DIR = "data/vedioprogram";
const std::string GLOBAL_HELP_DOC_DIR = "data/helpdoc";

const std::string GLOBAL_OTA_FILE_DIR = "data/ota_update";

const std::string GLOBAL_STATIC_URL = "http://47.92.192.154:9993/"
http://47.92.192.154:9077/iclean-cloud/data/download/map?device_id=22&uuid=af098920-acaf-4b76-8cfa-e8667ca392c3;*/

    public static enum Global{
        GLOBAL_STATIC_URL("GLOBAL_STATIC_URL","http://47.92.192.154:9993"),
        GLOBAL_MAP_PATH("GLOBAL_MAP_PATH","/data/map"),
        MAP_DOWNLOAD_URL("MAP_DOWNLOAD_URL","http://47.92.192.154:9077"),
        MAP_DOWNLOAD_PATH("MAP_DOWNLOAD_PATH","/iclean-cloud/data/download/map"),
        GLOBAL_LOGO_DIR("GLOBAL_LOGO_DIR","data/logo"),
        GLOBAL_HELPFILE_DIR("GLOBAL_HELPFILE_DIR","data/helpfile"),
        GLOBAL_HELP_DOC_DIR("GLOBAL_HELP_DOC_DIR","data/helpdoc"),
        GLOBAL_OTA_FILE_DIR("GLOBAL_OTA_FILE_DIR","data/ota_update"),
        GLOBAL_VEDIO_PROGRAM_DIR("GLOBAL_VEDIO_PROGRAM_DIR","data/vedioprogram");
        Global(String name,String value){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    /*FTP服务连接配置*/
    public static enum FtpServer{
        /*ftp.server.url=192.1687.11.76
ftp.server.port=21
ftp.server.username=iclean
ftp.server.password=123456*/
        FTP_SERVER_IP("FTP_SERVER_IP","192.168.11.76"),
        FTP_SERVER_PORT("FTP_SERVER_PORT","21"),
        FTP_SERVER_USERNAME("FTP_SERVER_USERNAME","iclean"),
        FTP_SERVER_PASWWORD("FTP_SERVER_PASWWORD","123456");

        FtpServer(String name,String value){
            this.value=value;
            this.name=name;
        }
        private final String value;
        private final String name;

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

}
