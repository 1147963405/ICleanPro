package com.iclean.pt.sbgl.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportJsonBean {

     private float topArea;
    private Integer startType;
    private Integer useSec;
    private Integer useElectricVol;
    private Integer topLenght;
    private String cmd_type;


    private String endTime;
    private Object path_list;
    private String task_name;
    private String map_name;
    private String stopReason;
    private String startTime;

    private Integer loopCount;
    private String pathFile;
    private Integer percentage;
    private float speed;




    /* "topArea": 24.7,
                    "startType": 1,
                    "useSec": 83,
                    "useElectricVol": 0,
                    "topLenght": 26.35,
                    "cmd_type": "clean_report",

                    "endTime": "2022-02-10 09:59:25",
                    "path_list": "http://47.92.192.154:9077/iclean-cloud/data/download/report_path?device_id=209&file_name=7daa2cc5-c4c3-4d71-b8bd-534a2f897051",
                    "task_name": "后门",
                    "map_name": "6",
                    "stopReason": "Finished",
                    "startTime": "2022-02-10 09:58:02",
                    "loopCount": 1,
                    "pathFile": "7daa2cc5-c4c3-4d71-b8bd-534a2f897051",
                    "percentage": 100,
                    "speed": 0.31747*/
}
