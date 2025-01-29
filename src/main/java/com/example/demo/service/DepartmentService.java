package com.example.demo.service;

// NOTE: Controllerから実装・単体テストを作成したりする際に有用なのでインターフェスを作っておく
// NOTE: テスト環境と開発環境と本番環境でServiceの実装を変えることなどもできる

/**
 * 部署サービス。
 */
public interface DepartmentService {

    /**
     * すべての部署結果を取得する。
     * 
     * @param operator 操作者
     * @return 部署結果
     */
    DepartmentResult fetchAll(String operator);

}
