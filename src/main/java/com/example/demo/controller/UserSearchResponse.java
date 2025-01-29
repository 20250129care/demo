package com.example.demo.controller;

import java.util.List;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ検索レスポンス。
 */
@Value
public class UserSearchResponse {

    /** データリスト。 */
    private List<UserSearchResponseData> list;

}
