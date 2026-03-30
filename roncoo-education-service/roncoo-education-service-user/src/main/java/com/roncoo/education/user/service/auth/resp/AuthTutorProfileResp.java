package com.roncoo.education.user.service.auth.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthTutorProfileResp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String realName;
    private Integer gender;
    private Integer tutorType;
    private Integer degree;
    private String university;
    private String major;
    private Integer auditStatus;
    private String subjects;
    private String grades;
    private String tags;
}
