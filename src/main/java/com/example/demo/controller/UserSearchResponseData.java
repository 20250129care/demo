package com.example.demo.controller;

import java.time.LocalDate;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ検索レスポンスデータ。
 */
@Value
public class UserSearchResponseData {

    /** フルネーム。 */
    private String name;

    /** 部署ID。 */
    private String deptId;

    /** 部署名。 */
    private String deptName;

    /** 最終更新日。 */
    private LocalDate lastUpdatedAt;

    /** ユーザID。 */
    private String userId;

    /** バージョン。 */
    private Integer userVersion;

}
