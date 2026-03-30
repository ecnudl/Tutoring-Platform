package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthStudentProfileResp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String realName;
    private Integer gender;
    private String grade;
    private String school;
    private String address;
    private String parentName;
    private String parentMobile;
}
