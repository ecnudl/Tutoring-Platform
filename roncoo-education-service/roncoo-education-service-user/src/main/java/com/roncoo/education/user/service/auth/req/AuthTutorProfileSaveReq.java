package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthTutorProfileSaveReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String avatar;
    private String realName;
    private Integer gender;
    private Integer tutorType;
    private Integer degree;
    private String university;
    private String major;
    private String gradeYear;
    private String selfIntroduction;
    private String subjects;
    private String grades;
    private String tags;
    private Long provinceId;
    private Long cityId;
    private Long districtId;
    private String address;
    private java.math.BigDecimal priceMin;
    private java.math.BigDecimal priceMax;
    private Integer freeTrial;
}
