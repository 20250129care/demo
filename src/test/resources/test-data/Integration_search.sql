DELETE FROM user;
DELETE FROM user_summary;

INSERT INTO user_summary (name, dept_id, dept_name, last_updated_at, user_id, user_version)
VALUES
    ('苗字名前', '01', '部署1', '2025-01-01', '20250101120055111_01', 0),
    ('苗字名前', '01', '部署1', '2025-01-01', '20250101120055111_02', 0),
    ('苗字名前', '01', '部署1', '2025-01-01', '20250101120055111_03', 0);
