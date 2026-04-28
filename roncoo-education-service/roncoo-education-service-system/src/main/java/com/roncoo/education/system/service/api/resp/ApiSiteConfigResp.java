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
    private String tutorGroupQrUrl;     // 教员接单群二维码 URL
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

    // 关于我们
    private String siteAboutIntroHtml;   // 公司简介

    // 帮助页正文
    private String helpRequestProcessHtml;   // 请家教流程
    private String helpFindTutorHtml;        // 怎样快速找到老师
    private String helpRequestFaqHtml;       // 请家教常见问题
    private String helpBecomeTutorHtml;      // 成为家教老师
    private String helpTutorFaqHtml;         // 做家教常见问题
    private String helpTutorPricingHtml;     // 做家教收费标准
    private String helpPaymentMethodHtml;    // 收费方式

    // 请家教表单
    private String qjjSubtitle;
    private String qjjSuccessTip;

    // JSON 列表
    private String siteFriendLinksJson;
    private String siteFooterMenusJson;
    private String sitePriceNotesJson;

    // 结构化家教价格（4 大类 × 若干表）
    private String sitePricingData;

    // 网站元数据（用于 <title>, <meta name=description/keywords>）
    private String websiteTitle;
    private String websiteDesc;
    private String websiteKeyword;
    private String websiteCopyright;
    private String websiteDomain;
}
