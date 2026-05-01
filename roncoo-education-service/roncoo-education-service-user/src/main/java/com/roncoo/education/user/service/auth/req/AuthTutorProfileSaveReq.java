package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class AuthTutorProfileSaveReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String avatar;
    private String realName;
    private Integer gender;
    private LocalDate birthDate;
    private String idCard;
    private Integer tutorType;
    private String identityDetail;
    private Integer degree;
    private String university;
    private Long universityId;
    private String major;
    private String gradeYear;
    private String highSchool;
    private String hometownProvince;
    private String email;
    private String wechat;
    private String selfIntroduction;
    private String certificatesDesc;
    private Integer teachingMethod;
    private String subjects;
    private String grades;
    private String tags;
    private Long provinceId;
    private Long cityId;
    private Long districtId;
    private String address;
    private java.math.BigDecimal priceMin;
    private java.math.BigDecimal priceMax;
    private String salaryRemark;
    private Integer freeTrial;
    private Integer showSuccessRecord;
}
