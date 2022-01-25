package com.iclean.pt.sbgl.bean;

//entity层实体类

import java.util.List;

//分页展示
//相关属性：当前页，页大小(每页显示的条数)，总页数，总条数，数据
//select * from t_user limit 3,3
public class Page {
    private Integer currentPage;  //当前页
    private Integer pageSize;     //页大小
    private Integer pageCount;   //页数量
    private Integer totalCount;  //总条数
    private List<?> list;        //数据
    private String  url;         //参数路径
    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getPageCount() {
        return pageCount;
    }
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
    public Integer getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    public List<?> getList() {
        return list;
    }
    public void setList(List<?> list) {
        this.list = list;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Page [currentPage=" + currentPage + ", pageSize=" + pageSize + ", pageCount=" + pageCount
                + ", totalCount=" + totalCount + ", list=" + list + ", url=" + url + "]";
    }

    public Page(Integer currentPage, Integer pageSize, Integer pageCount, Integer totalCount, List<?> list,
                String url) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.totalCount = totalCount;
        this.list = list;
        this.url = url;
    }
    public Page(Integer currentPage, Integer pageSize, Integer pageCount, List<?> list) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.list = list;
    }

    public Page() {
    }

    public Page(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
