package com.example.demo.service;

import java.util.List;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * 複数のユーザ更新パラメータ。
 */
@Value
public class UserBulkUpdateParam {

    /** ユーザ更新パラメータリスト。 */
    private List<UserUpdateParam> list;

}
