package com.roncoo.education.user.service.auth.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AuthRequirementPageReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer pageCurrent = 1;
    private Integer pageSize = 20;
    private Integer reqStatus;
}
