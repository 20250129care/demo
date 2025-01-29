package com.example.demo.service;

// NOTE: Controllerから実装・単体テストを作成したりする際に有用なのでインターフェスを作っておく
// NOTE: テスト環境と開発環境と本番環境でServiceの実装を変えることなどもできる

/**
 * ユーザ検索サービス。
 */
public interface UserSearchService {

    /**
     * ユーザを検索する。
     * 
     * @param operator 操作者
     * @param param ユーザ検索パラメータ
     * @return ユーザ検索結果
     */
    UserSearchResult search(String operator, UserSearchParam param);

}
