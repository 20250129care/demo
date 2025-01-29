DELIMITER $$

CREATE PROCEDURE IF NOT EXISTS generate_user_summary_data()
BEGIN
    DECLARE i INT DEFAULT 0;

    START TRANSACTION;

    WHILE i < 100000000 DO
        INSERT INTO user_summary (name, dept_id, dept_name, last_updated_at, user_id, user_version)
        VALUES
            (
                CONCAT('name', LPAD((i % 10000000) + 1, 8, '0')),
                LPAD((i % 10) + 1, 2, '0'),
                CONCAT('dept', LPAD((i % 10) + 1, 2, '0')),
                DATE_ADD('2025-04-01', INTERVAL (i % 30) DAY),
                CONCAT(DATE_FORMAT(DATE_ADD('2025-04-01 12:00:00', INTERVAL i MINUTE), '%Y%m%d%H%i%S'), '000_01'),
                0
            );

        SET i = i + 1;
    END WHILE;

    COMMIT;
END$$

CREATE PROCEDURE IF NOT EXISTS generate_user_data()
BEGIN
    DECLARE i INT DEFAULT 0;

    START TRANSACTION;

    WHILE i < 100000000 DO
        INSERT INTO user (id, family_name, first_name, dept_id, version)
        VALUES
            (
                CONCAT(DATE_FORMAT(DATE_ADD('2025-04-01 12:00:00', INTERVAL i MINUTE), '%Y%m%d%H%i%S'), '000_01'),
                'family',
                CONCAT('name', LPAD((i % 10000000) + 1, 8, '0')),
                LPAD((i % 10) + 1, 2, '0'),
                0
            );

        SET i = i + 1;
    END WHILE;

    COMMIT;
END$$

DELIMITER ;
