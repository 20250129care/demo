package com.example.demo.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Value;

// NOTE: リクエストのバリデーションチェックを書く。詳細設計書のバリデーションチェックと照らし合わせて書く。
// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ作成リクエスト。
 */
@Value
public class UserCreateRequest {

    /** 苗字。 */
    @NotNull
    @Length(min = 1, max = 50)
    private String familyName;

    /** 名前。 */
    @NotNull
    @Length(min = 1, max = 50)
    private String firstName;

    // NOTE: messageに値を{}で囲んで定義することでValidationMessages.propertiesに定義しているメッセージを表示できる

    /** 部署ID。 */
    @NotNull
    @Pattern(regexp = "^[0-9]{2}$", message = "{my.DeptIdLength.message}")
    private String deptId;

}
