package com.example.demo.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Value;

// NOTE: リクエストのバリデーションチェックを書く。詳細設計書のバリデーションチェックと照らし合わせて書く。
// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * 複数のユーザ更新リクエスト。
 */
@Value
public class UserBulkUpdateRequest {

    // NOTE: ネストしている項目には@Validが必須

    /** ユーザ更新リクエストリスト。 */
    @Valid
    @NotNull
    @Size(min = 1, max = 10)
    private List<UserUpdateRequest> list;

}
