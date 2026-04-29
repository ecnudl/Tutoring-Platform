-- VIP 清理: 删菜单 + 删表 (vip_membership 0 行)
DELETE FROM sys_menu WHERE id = 800108;
DROP TABLE IF EXISTS vip_membership;
