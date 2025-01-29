package com.example.demo.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Value;

// NOTE: リクエストのバリデーションチェックを書く。詳細設計書のバリデーションチェックと照らし合わせて書く。
// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ更新リクエスト。
 */
@Value
public class UserUpdateRequest {

    // NOTE: messageに値を{}で囲んで定義することでValidationMessages.propertiesに定義しているメッセージを表示できる

    /** ユーザID */
    @NotNull
    @Pattern(regexp = "^[0-9]{17}_[0-9]{2}$", message = "{my.UserIdLength.message}")
    private String id;

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

    /** バージョン。 */
    @NotNull
    @Max(999)
    @Min(0)
    private Integer version;

}
