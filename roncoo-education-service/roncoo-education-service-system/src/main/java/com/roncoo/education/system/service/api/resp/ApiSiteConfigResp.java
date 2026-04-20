package com.roncoo.education.system.service.api.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 站点公开配置。通过 SysConfigCommonBiz.getSysConfig(Class) 按字段名自动从 sys_config 表装配。
 */
@Data
@Accessors(chain = true)
public class ApiSiteConfigResp implements Serializable {
    private static final long serialVersionUID = 1L;

    // 品牌
    private String siteBrandName;
    private String siteBrandSlogan;

    // 客服
    private String siteHotline;
    private String siteCsEmail;
    private String siteCsWechat;
    private String siteWorkTime;

    // 备案
    private String siteIcpNo;
    private String sitePublicSecurityNo;

    // 底部 & 版权
    private String siteFooterDesc;
    private String siteCopyright;

    // 协议正文
    private String agreementUserHtml;
    private String agreementDisclaimerHtml;
    private String agreementPrivacyHtml;

    // 请家教表单
    private String qjjSubtitle;
    private String qjjSuccessTip;

    // JSON 列表
    private String siteFriendLinksJson;
    private String siteFooterMenusJson;
    private String sitePriceNotesJson;
}
