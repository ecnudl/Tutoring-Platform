package com.roncoo.education.user.service.api.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * API-需求搜索
 * </p>
 *
 * @author fengyw
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "API-需求搜索")
public class RequirementSearchReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页")
    private int pageCurrent = 1;

    @ApiModelProperty(value = "每页条数")
    private int pageSize = 20;

    @ApiModelProperty(value = "科目ID")
    private Long subjectId;

    @ApiModelProperty(value = "年级ID")
    private Long gradeId;

    @ApiModelProperty(value = "省份ID")
    private Long provinceId;

    @ApiModelProperty(value = "城市ID")
    private Long cityId;

    @ApiModelProperty(value = "区县ID (历史) ⚠ 已弃用, 用 district 名字")
    private Long districtId;

    @ApiModelProperty(value = "区域名 (单选, 用于列表筛选, 匹配 district_names CSV)")
    private String district;

    @ApiModelProperty(value = "科目名 (单选, 匹配 subject_ids CSV)")
    private String subject;

    @ApiModelProperty(value = "教员类型名 (大学生 / 专职教员 / 在职教师, 匹配 tutor_type_pref CSV)")
    private String tutorType;

    @ApiModelProperty(value = "授课方式(1:教员上门,2:学员上门,3:线上授课)")
    private Integer teachingMethod;

    @ApiModelProperty(value = "是否有折扣(0:否,1:是)")
    private Integer hasDiscount;

    @ApiModelProperty(value = "院校 (兼容字段, 当前未实现)")
    private String university;

    @ApiModelProperty(value = "关键词")
    private String keyword;
}
