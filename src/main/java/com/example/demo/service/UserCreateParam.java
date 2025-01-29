package com.example.demo.service;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ作成パラメータ。
 */
@Value
public class UserCreateParam {

    /** 苗字。 */
    private String familyName;

    /** 名前。 */
    private String firstName;

    /** 部署ID。 */
    private String deptId;

}
