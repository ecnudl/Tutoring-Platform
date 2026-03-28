package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthRequirementSaveReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String subjectIds;
    private Long gradeId;
    private Long provinceId;
    private Long cityId;
    private Long districtId;
    private String address;
    private Integer tutorGender;
    private String schedule;
    private java.math.BigDecimal budgetMin;
    private java.math.BigDecimal budgetMax;
    private String requirementDetail;
    private String contactName;
    private String contactMobile;
}
