package com.example.demo.service;

import com.example.demo.common.exception.ApplicationException;

// NOTE: Controllerから実装・単体テストを作成したりする際に有用なのでインターフェスを作っておく
// NOTE: テスト環境と開発環境と本番環境でServiceの実装を変えることなどもできる

/**
 * ユーザ作成サービス。
 */
public interface UserCreateService {

    /**
     * ユーザを作成する。
     * 
     * @param operator 操作者
     * @param param ユーザ作成パラメータ
     * @throws ApplicationException 業務エラー
     */
    void create(String operator, UserCreateParam param) throws ApplicationException;

}
