package com.example.demo.service;

import java.time.LocalDate;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ検索パラメータ。
 */
@Value
public class UserSearchParam {

    /** フルネーム。 */
    private String name;

    /** 部署ID。 */
    private String deptId;

    /** 更新日（開始）。 */
    private LocalDate beginUpdatedAt;

    /** 更新日（終了）。 */
    private LocalDate endUpdatedAt;

    /** ページ番号。1から始まる。 */
    private Integer pageNo;

    /** 1ページで表示する件数。最大100件。 */
    private Integer pageSize;

}
