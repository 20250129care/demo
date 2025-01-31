# SpringBoot勉強用プロジェクト

## 事前準備

### Windows

docker\mysql\my.cnfを読み取り専用にする。読み取り専用でないとMySQLで設定が読み込まれない。

### Linux/Mac

```bash
# アクセス権限を644にする。644でないとMySQLで設定が読み込まれない。
chmod 644 docker/mysql/my.cnf
```

## 基本操作

```bash
# Docker起動
docker compose up -d

# Docker停止
docker compose stop
```

## ログ表示

```bash
# アプリのログ表示
docker compose logs -f app

# MySQLのログ表示
docker compose logs -f mysql

# WireMockのログ表示
docker compose logs -f wiremock
```

## 再起動

```bash
# アプリ再起動
docker compose restart app

# MySQL再起動
docker compose restart mysql

# WireMock
docker compose restart wiremock
```

## サンプル

```bash
# ユーザ一覧取得
curl -X POST -H "X-Operator: 0001" -H "Content-Type: application/json" -d "{\"begin_updated_at\":\"2025-01-01\",\"page_no\":1,\"page_size\":100}"  http://localhost:8080/api/users/search

# ユーザ作成
curl -X POST -H "X-Operator: 0001" -H "Content-Type: application/json" -d "{\"family_name\":\"田中\",\"first_name\":\"太郎\",\"dept_id\":\"01\"}" http://localhost:8080/api/users/create

# 複数ユーザ作成
curl -X POST -H "X-Operator: 0001" -H "Content-Type: application/json" -d "{\"list\":[{\"family_name\":\"田中\",\"first_name\":\"太郎１\",\"dept_id\":\"01\"},{\"family_name\":\"田中\",\"first_name\":\"太郎２\",\"dept_id\":\"01\"},{\"family_name\":\"田中\",\"first_name\":\"太郎３\",\"dept_id\":\"01\"}]}" http://localhost:8080/api/users/bulk/create

# ユーザ更新
curl -X POST -H "X-Operator: 0001" -H "Content-Type: application/json" -d "{\"id\":\"{ID}\",\"family_name\":\"山田\",\"first_name\":\"花子\",\"dept_id\":\"01\",\"version\":0}" http://localhost:8080/api/users/update

# 複数ユーザ更新
curl -X POST -H "X-Operator: 0001" -H "Content-Type: application/json" -d "{\"list\":[{\"id\":\"{ID}\",\"family_name\":\"山田\",\"first_name\":\"花子１\",\"dept_id\":\"01\",\"version\":0},{\"id\":\"{ID}\",\"family_name\":\"山田\",\"first_name\":\"花子２\",\"dept_id\":\"01\",\"version\":0},{\"id\":\"{ID}}\",\"family_name\":\"山田\",\"first_name\":\"花子３\",\"dept_id\":\"01\",\"version\":0}]}" http://localhost:8080/api/users/bulk/update

# すべての部署取得
curl -X GET -H "X-Operator: 0001" http://localhost:8080/api/departments

# ヘルスチェック
curl http://localhost:8080/api/actuator/health
```

## ファイル出力

```
# ログ出力ディレクトリ
~/log/app

# テスト結果出力ディレクトリ
./build/reports/tests

# カバレッジ出力ディレクトリ
./build/reports/jacoco
```
