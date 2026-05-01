-- 清理 800999 (admin 全接口白名单) 的冗余 apis: 之前为修 tutor-audit 权限把所有缺失接口都塞进来了,
-- 现在 city/district/university/audit-log/homepage-config/price-reference/subject-category 已从前端删除,
-- 只保留 7 个仍在用的 tutor-audit 新接口.
UPDATE sys_menu SET apis = '/user/admin/tutor-audit/approve,/user/admin/tutor-audit/cert/approve,/user/admin/tutor-audit/cert/list,/user/admin/tutor-audit/cert/reject,/user/admin/tutor-audit/recent-edited,/user/admin/tutor-audit/reject,/user/admin/tutor-audit/star' WHERE id = 800999;

SELECT id, menu_name, apis FROM sys_menu WHERE id = 800999;
