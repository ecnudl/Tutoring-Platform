-- 学员订单 display_no 前缀 A → S
UPDATE tutor_requirement
SET display_no = CONCAT('S', SUBSTRING(display_no, 2))
WHERE display_no LIKE 'A%';
