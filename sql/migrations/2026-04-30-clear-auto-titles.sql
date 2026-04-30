-- 清掉 AuthReservationBiz 早期自动写入的 "定向预约 X 教员 (Txxx)" title.
-- 这些是后端自动生成的, 现在改由 admin 手填, 所以清空让前端走科目降级逻辑.
UPDATE tutor_requirement SET title = NULL WHERE title LIKE '定向预约 %教员%';
SELECT id, display_no, title FROM tutor_requirement WHERE title IS NOT NULL OR display_no LIKE 'S%';
