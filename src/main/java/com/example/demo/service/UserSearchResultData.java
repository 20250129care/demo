package com.example.demo.service;

import java.time.LocalDate;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ検索結果データ。
 */
@Value
public class UserSearchResultData {

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
