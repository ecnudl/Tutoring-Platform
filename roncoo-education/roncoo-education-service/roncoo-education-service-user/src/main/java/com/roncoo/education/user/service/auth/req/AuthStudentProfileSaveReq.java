package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthStudentProfileSaveReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String realName;
    private Integer gender;
    private String grade;
    private String school;
    private Long provinceId;
    private Long cityId;
    private Long districtId;
    private String address;
    private String parentName;
    private String parentMobile;
}
