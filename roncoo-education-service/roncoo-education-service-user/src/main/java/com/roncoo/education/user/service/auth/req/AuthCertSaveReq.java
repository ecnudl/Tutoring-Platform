package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthCertSaveReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long tutorId;
    private Integer certType;
    private String certName;
    private String certUrl;
    private String certNo;
}
