package com.example.demo.controller;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Value;

// NOTE: リクエストのバリデーションチェックを書く。詳細設計書のバリデーションチェックと照らし合わせて書く。
// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ検索リクエスト。
 */
@Value
public class UserSearchRequest {

    /** フルネーム。 */
    @Length(min = 1, max = 100)
    private String name;

    // NOTE: messageに値を{}で囲んで定義することでValidationMessages.propertiesに定義しているメッセージを表示できる

    /** 部署ID。 */
    @Pattern(regexp = "^[0-9]{2}$", message = "{my.DeptIdLength.message}")
    private String deptId;

    /** 更新日（開始）。 */
    private LocalDate beginUpdatedAt;

    /** 更新日（終了）。 */
    private LocalDate endUpdatedAt;

}
