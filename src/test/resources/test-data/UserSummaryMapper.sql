DELETE FROM user;
DELETE FROM user_summary;

INSERT INTO user (id, family_name, first_name, dept_id, version)
VALUES
    -- 挿入可能なデータ
    ('20250701094512000_01', '苗字', '名前1', '02', 0),
    ('20250701094512000_02', '苗字', '名前2', '02', 0),
    ('20250701094512000_03', '苗字', '名前3', '02', 0),
    -- 更新可能なデータ
    ('20250701183006000_01', '苗字', '名前101', '03', 1),
    ('20250701183006000_02', '苗字', '名前102', '03', 1),
    ('20250701183006000_03', '苗字', '名前103', '03', 1);

INSERT INTO user_summary (name, dept_id, dept_name, last_updated_at, user_id, user_version)
VALUES
    -- 検索可能なデータ
    ('苗字名前0', '01', '部署1', '2025-01-01', '20250101120055111_01', 0),
    ('苗字名前0', '01', '部署1', '2025-07-01', '20250701192423499_01', 0),
    ('苗字名前0', '01', '部署1', '2025-12-31', '20251231063011222_01', 0),
    -- 更新対象のデータ
    ('苗字名前1', '03', '部署3', '2025-07-01', '20250701183006000_01', 0),
    ('苗字名前2', '03', '部署3', '2025-07-01', '20250701183006000_02', 0),
    ('苗字名前3', '03', '部署3', '2025-07-01', '20250701183006000_03', 0);
