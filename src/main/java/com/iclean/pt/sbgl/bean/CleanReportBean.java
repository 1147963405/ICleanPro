package com.iclean.pt.sbgl.bean;

public class CleanReportBean {
    private Integer id;

    private Integer mapId;

    private Integer deviceId;

    private String name;

    private Float cleanArea;

    private Integer useTime;

    private Integer type;

    private String report;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Float getCleanArea() {
        return cleanArea;
    }

    public void setCleanArea(Float cleanArea) {
        this.cleanArea = cleanArea;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report == null ? null : report.trim();
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CleanReportBean{" +
                "id=" + id +
                ", mapId=" + mapId +
                ", deviceId=" + deviceId +
                ", name='" + name + '\'' +
                ", cleanArea=" + cleanArea +
                ", useTime=" + useTime +
                ", type=" + type +
                ", report='" + report + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}