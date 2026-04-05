package com.roncoo.education.user.dao.impl.mapper.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DictCity implements Serializable {
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private String cityName;
    private String cityPinyin;
    private String provinceName;
    private Integer isHot;
    private Integer sort;
    private static final long serialVersionUID = 1L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(LocalDateTime gmtCreate) { this.gmtCreate = gmtCreate; }
    public LocalDateTime getGmtModified() { return gmtModified; }
    public void setGmtModified(LocalDateTime gmtModified) { this.gmtModified = gmtModified; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName == null ? null : cityName.trim(); }
    public String getCityPinyin() { return cityPinyin; }
    public void setCityPinyin(String cityPinyin) { this.cityPinyin = cityPinyin == null ? null : cityPinyin.trim(); }
    public String getProvinceName() { return provinceName; }
    public void setProvinceName(String provinceName) { this.provinceName = provinceName == null ? null : provinceName.trim(); }
    public Integer getIsHot() { return isHot; }
    public void setIsHot(Integer isHot) { this.isHot = isHot; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }

    @Override
    public String toString() { return "DictCity [id=" + id + ", cityName=" + cityName + "]"; }
}
