package com.example.demo.service;

import java.util.List;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * 複数のユーザ作成パラメータ。
 */
@Value
public class UserBulkCreateParam {

    /** ユーザ作成パラメータリスト。 */
    private List<UserCreateParam> list;

}
