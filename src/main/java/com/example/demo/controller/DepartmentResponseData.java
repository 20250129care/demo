package com.example.demo.controller;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * 部署レスポンスデータ。
 */
@Value
public class DepartmentResponseData {

    /** 部署ID。 */
    private String id;

    /** 部署名。 */
    private String name;

    /** 削除済み。 */
    private Boolean isDeleted;

}
