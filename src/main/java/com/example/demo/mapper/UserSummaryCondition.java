package com.example.demo.mapper;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: Entityは値の設定を行うために可変の@Dataとする

/**
 * ユーザ概要の検索条件。
 */
@AllArgsConstructor
@Data
public class UserSummaryCondition {

    /** フルネーム。 */
    private String name;

    /** 部署ID。 */
    private String deptId;

    /** 更新日（開始）。 */
    private LocalDate beginUpdatedAt;

    /** 更新日（終了）。 */
    private LocalDate endUpdatedAt;

}
