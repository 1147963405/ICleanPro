package com.iclean.pt.sbgl.bean;

public class TaskBean {
    private Integer id;

    private String name;

    private Integer mapId;

    private Integer deviceId;

    private Integer type;

    private Integer workDay;

    private Integer workTime;

    private Integer workType;

    private Integer loopCount;

    private String parameter;

    private String workParts;

    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWorkDay() {
        return workDay;
    }

    public void setWorkDay(Integer workDay) {
        this.workDay = workDay;
    }

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public Integer getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(Integer loopCount) {
        this.loopCount = loopCount;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }

    public String getWorkParts() {
        return workParts;
    }

    public void setWorkParts(String workParts) {
        this.workParts = workParts == null ? null : workParts.trim();
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mapId=" + mapId +
                ", deviceId=" + deviceId +
                ", type=" + type +
                ", workDay=" + workDay +
                ", workTime=" + workTime +
                ", workType=" + workType +
                ", loopCount=" + loopCount +
                ", parameter='" + parameter + '\'' +
                ", workParts='" + workParts + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}