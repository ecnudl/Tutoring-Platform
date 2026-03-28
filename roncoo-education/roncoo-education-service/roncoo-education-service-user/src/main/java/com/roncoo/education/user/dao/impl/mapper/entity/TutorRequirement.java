package com.roncoo.education.user.dao.impl.mapper.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TutorRequirement implements Serializable {
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private Long userId;
    private String title;
    private String subjectIds;
    private Long gradeId;
    private Long provinceId;
    private Long cityId;
    private Long districtId;
    private String address;
    private Integer tutorGender;
    private String tutorTypePref;
    private String schedule;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String requirementDetail;
    private String contactName;
    private String contactMobile;
    private Integer reqStatus;
    private String auditRemark;
    private Integer viewCount;
    private Integer applicationCount;
    private static final long serialVersionUID = 1L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(LocalDateTime gmtCreate) { this.gmtCreate = gmtCreate; }
    public LocalDateTime getGmtModified() { return gmtModified; }
    public void setGmtModified(LocalDateTime gmtModified) { this.gmtModified = gmtModified; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title == null ? null : title.trim(); }
    public String getSubjectIds() { return subjectIds; }
    public void setSubjectIds(String subjectIds) { this.subjectIds = subjectIds == null ? null : subjectIds.trim(); }
    public Long getGradeId() { return gradeId; }
    public void setGradeId(Long gradeId) { this.gradeId = gradeId; }
    public Long getProvinceId() { return provinceId; }
    public void setProvinceId(Long provinceId) { this.provinceId = provinceId; }
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    public Long getDistrictId() { return districtId; }
    public void setDistrictId(Long districtId) { this.districtId = districtId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address == null ? null : address.trim(); }
    public Integer getTutorGender() { return tutorGender; }
    public void setTutorGender(Integer tutorGender) { this.tutorGender = tutorGender; }
    public String getTutorTypePref() { return tutorTypePref; }
    public void setTutorTypePref(String tutorTypePref) { this.tutorTypePref = tutorTypePref == null ? null : tutorTypePref.trim(); }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule == null ? null : schedule.trim(); }
    public BigDecimal getBudgetMin() { return budgetMin; }
    public void setBudgetMin(BigDecimal budgetMin) { this.budgetMin = budgetMin; }
    public BigDecimal getBudgetMax() { return budgetMax; }
    public void setBudgetMax(BigDecimal budgetMax) { this.budgetMax = budgetMax; }
    public String getRequirementDetail() { return requirementDetail; }
    public void setRequirementDetail(String requirementDetail) { this.requirementDetail = requirementDetail == null ? null : requirementDetail.trim(); }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName == null ? null : contactName.trim(); }
    public String getContactMobile() { return contactMobile; }
    public void setContactMobile(String contactMobile) { this.contactMobile = contactMobile == null ? null : contactMobile.trim(); }
    public Integer getReqStatus() { return reqStatus; }
    public void setReqStatus(Integer reqStatus) { this.reqStatus = reqStatus; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark == null ? null : auditRemark.trim(); }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Integer getApplicationCount() { return applicationCount; }
    public void setApplicationCount(Integer applicationCount) { this.applicationCount = applicationCount; }

    @Override
    public String toString() {
        return "TutorRequirement [id=" + id + ", userId=" + userId + ", title=" + title + ", reqStatus=" + reqStatus + "]";
    }
}
