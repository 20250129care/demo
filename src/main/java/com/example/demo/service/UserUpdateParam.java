package com.example.demo.service;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ更新パラメータ。
 */
@Value
public class UserUpdateParam {

    /** ユーザID */
    private String id;

    /** 苗字。 */
    private String familyName;

    /** 名前。 */
    private String firstName;

    /** 部署ID。 */
    private String deptId;

    /** バージョン。 */
    private Integer version;

}
