-- 补 sys_menu 缺失的 800108 公告管理菜单 (sys_menu_role 已绑给超管, 但 sys_menu 里没这条)
INSERT INTO sys_menu (id, parent_id, status_id, sort, is_show, menu_type, menu_name, path, apis, permission)
VALUES (
  800108, 800000, 1, 8, b'1', 1,
  '公告管理',
  '/content/announcement',
  '/system/admin/announcement/page,/system/admin/announcement/view,/system/admin/announcement/save,/system/admin/announcement/edit,/system/admin/announcement/delete',
  'announcement:admin'
)
ON DUPLICATE KEY UPDATE
  apis = VALUES(apis),
  menu_name = VALUES(menu_name),
  path = VALUES(path),
  permission = VALUES(permission);

-- 验证
SELECT id, parent_id, menu_name, path, apis FROM sys_menu WHERE id=800108;
