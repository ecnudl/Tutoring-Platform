package com.roncoo.education.user.dao.impl.mapper.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TutorCertification implements Serializable {
    private Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Integer statusId;
    private Long tutorId;
    private Integer certType;
    private String certName;
    private String certUrl;
    private String certNo;
    private Integer auditStatus;
    private String auditRemark;
    private static final long serialVersionUID = 1L;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(LocalDateTime gmtCreate) { this.gmtCreate = gmtCreate; }
    public LocalDateTime getGmtModified() { return gmtModified; }
    public void setGmtModified(LocalDateTime gmtModified) { this.gmtModified = gmtModified; }
    public Integer getStatusId() { return statusId; }
    public void setStatusId(Integer statusId) { this.statusId = statusId; }
    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }
    public Integer getCertType() { return certType; }
    public void setCertType(Integer certType) { this.certType = certType; }
    public String getCertName() { return certName; }
    public void setCertName(String certName) { this.certName = certName == null ? null : certName.trim(); }
    public String getCertUrl() { return certUrl; }
    public void setCertUrl(String certUrl) { this.certUrl = certUrl == null ? null : certUrl.trim(); }
    public String getCertNo() { return certNo; }
    public void setCertNo(String certNo) { this.certNo = certNo == null ? null : certNo.trim(); }
    public Integer getAuditStatus() { return auditStatus; }
    public void setAuditStatus(Integer auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditRemark() { return auditRemark; }
    public void setAuditRemark(String auditRemark) { this.auditRemark = auditRemark == null ? null : auditRemark.trim(); }

    @Override
    public String toString() {
        return "TutorCertification [id=" + id + ", tutorId=" + tutorId + ", certType=" + certType + ", certName=" + certName + "]";
    }
}
