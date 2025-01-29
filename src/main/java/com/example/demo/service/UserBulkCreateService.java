package com.example.demo.service;

import com.example.demo.common.exception.ApplicationException;

// NOTE: Controllerから実装・単体テストを作成したりする際に有用なのでインターフェスを作っておく
// NOTE: テスト環境と開発環境と本番環境でServiceの実装を変えることなどもできる

/**
 * 複数のユーザ作成サービス。
 */
public interface UserBulkCreateService {

    /**
     * 複数のユーザを作成する。
     * 
     * @param operator 操作者
     * @param param 複数のユーザ作成パラメータ
     * @throws ApplicationException 業務エラー
     */
    void bulkCreate(String operator, UserBulkCreateParam param) throws ApplicationException;

}
