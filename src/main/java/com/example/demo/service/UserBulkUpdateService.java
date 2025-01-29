package com.example.demo.service;

import com.example.demo.common.exception.ApplicationException;

// NOTE: Controllerから実装・単体テストを作成したりする際に有用なのでインターフェスを作っておく
// NOTE: テスト環境と開発環境と本番環境でServiceの実装を変えることなどもできる

/**
 * 複数のユーザ更新サービス。
 */
public interface UserBulkUpdateService {

    /**
     * 複数のユーザを更新する。
     * 
     * @param operator 操作者
     * @param bulkParam 複数のユーザ更新パラメータ
     * @throws ApplicationException 業務エラー
     */
    void bulkUpdate(String operator, UserBulkUpdateParam bulkParam) throws ApplicationException;

}
