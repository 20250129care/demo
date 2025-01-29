package com.example.demo.mapper;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: Entityは値の設定を行うために可変の@Dataとする

/**
 * ユーザ概要エンティティ。
 */
@AllArgsConstructor
@Data
public class UserSummary {

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
