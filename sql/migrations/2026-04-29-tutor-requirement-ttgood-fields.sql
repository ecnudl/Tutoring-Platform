-- tutor_requirement: 6 个新列以支持 ttgood 风格订单详情
-- MySQL 8.x 不支持 ADD COLUMN IF NOT EXISTS, 用存储过程做幂等

DELIMITER $$
DROP PROCEDURE IF EXISTS tr_add_col_if_missing$$
CREATE PROCEDURE tr_add_col_if_missing(IN col VARCHAR(64), IN spec TEXT)
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'tutor_requirement' AND column_name = col
  ) THEN
    SET @sql := CONCAT('ALTER TABLE `tutor_requirement` ADD COLUMN `', col, '` ', spec);
    PREPARE st FROM @sql; EXECUTE st; DEALLOCATE PREPARE st;
  END IF;
END$$
DELIMITER ;

CALL tr_add_col_if_missing('student_gender',
  "TINYINT DEFAULT 0 COMMENT '学员性别 0未知 1男 2女'");
CALL tr_add_col_if_missing('transport',
  "VARCHAR(200) DEFAULT NULL COMMENT '交通线路 (自由文本)'");
CALL tr_add_col_if_missing('frequency',
  "VARCHAR(50) DEFAULT NULL COMMENT '频次 (如 5次/周)'");
CALL tr_add_col_if_missing('requirement_type',
  "TINYINT DEFAULT 0 COMMENT '求教性质 1提高型 2同步辅导 3竞赛 4冲刺 5陪学 6其他'");
CALL tr_add_col_if_missing('other_requirements',
  "VARCHAR(500) DEFAULT NULL COMMENT '其它要求 (对教员的额外要求)'");
CALL tr_add_col_if_missing('transport_subsidy',
  "VARCHAR(100) DEFAULT NULL COMMENT '有无车贴 (自由文本: 无/有, 单次30元)'");

DROP PROCEDURE IF EXISTS tr_add_col_if_missing;
