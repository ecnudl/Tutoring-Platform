package com.roncoo.education.user.service.api.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * API-科目分类树
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-科目分类树")
public class SubjectTreeResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "分类ID")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "科目列表")
    private List<SubjectItem> subjects;

    @Data
    @Accessors(chain = true)
    @ApiModel(description = "科目信息")
    public static class SubjectItem implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "科目ID")
        private Long id;

        @ApiModelProperty(value = "科目名称")
        private String subjectName;

        @ApiModelProperty(value = "是否热门(0:否,1:是)")
        private Integer isHot;
    }
}
