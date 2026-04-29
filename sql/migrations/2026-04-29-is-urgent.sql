-- tutor_requirement 加 is_urgent 字段 (0 普通 / 1 加急)
DELIMITER $$
DROP PROCEDURE IF EXISTS tr_add_col_urgent$$
CREATE PROCEDURE tr_add_col_urgent()
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'tutor_requirement' AND column_name = 'is_urgent'
  ) THEN
    ALTER TABLE `tutor_requirement` ADD COLUMN `is_urgent` TINYINT NOT NULL DEFAULT 0 COMMENT '是否加急 0否 1是';
  END IF;
END$$
DELIMITER ;
CALL tr_add_col_urgent();
DROP PROCEDURE tr_add_col_urgent;
