-- 修复 admin "没此权限" 系统性问题: 一次性补全前端调用但 sys_menu apis 缺失的所有接口.
-- 建一条隐藏 menu (is_show=0, sort=999) 作为超管的全接口白名单容器.
INSERT INTO sys_menu (id, parent_id, status_id, sort, is_show, menu_type, menu_name, apis)
VALUES (
  800999, 800000, 1, 999, b'0', 1, '_admin全接口白名单_(请勿删除)',
  '/system/admin/audit-log/page,/system/admin/city/delete,/system/admin/city/edit,/system/admin/city/page,/system/admin/city/save,/system/admin/district/delete,/system/admin/district/edit,/system/admin/district/page,/system/admin/district/save,/system/admin/faq/delete,/system/admin/faq/edit,/system/admin/faq/page,/system/admin/faq/save,/system/admin/faq/view,/system/admin/homepage-config/batch-save,/system/admin/homepage-config/list,/system/admin/price-reference/delete,/system/admin/price-reference/edit,/system/admin/price-reference/page,/system/admin/price-reference/save,/system/admin/subject-category/delete,/system/admin/subject-category/edit,/system/admin/subject-category/page,/system/admin/subject-category/save,/system/admin/university/delete,/system/admin/university/edit,/system/admin/university/page,/system/admin/university/save,/user/admin/tutor-audit/approve,/user/admin/tutor-audit/cert/approve,/user/admin/tutor-audit/cert/list,/user/admin/tutor-audit/cert/reject,/user/admin/tutor-audit/recent-edited,/user/admin/tutor-audit/reject,/user/admin/tutor-audit/star'
)
ON DUPLICATE KEY UPDATE apis = VALUES(apis), menu_name = VALUES(menu_name), is_show = b'0';

INSERT IGNORE INTO sys_menu_role (status_id, sort, menu_id, role_id) VALUES (1, 999, 800999, 1);

-- 验证
SELECT id, menu_name, LENGTH(apis) AS apis_len FROM sys_menu WHERE id=800999;
SELECT COUNT(*) AS bound_to_super_admin FROM sys_menu_role WHERE menu_id=800999 AND role_id=1;
