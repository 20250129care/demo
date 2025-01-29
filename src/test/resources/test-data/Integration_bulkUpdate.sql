DELETE FROM user;
DELETE FROM user_summary;

INSERT INTO user (id, family_name, first_name, dept_id, version)
VALUES
    ('20250101120055111_01', '苗字1', '名前1', '01', 0),
    ('20250101120055111_02', '苗字2', '名前2', '01', 0),
    ('20250101120055111_03', '苗字3', '名前3', '01', 0);

INSERT INTO user_summary (name, dept_id, dept_name, last_updated_at, user_id, user_version)
VALUES
    ('苗字1名前1', '01', '部署1', '2025-01-01', '20250101120055111_01', 0),
    ('苗字2名前2', '01', '部署1', '2025-01-01', '20250101120055111_02', 0),
    ('苗字3名前3', '01', '部署1', '2025-01-01', '20250101120055111_03', 0);
