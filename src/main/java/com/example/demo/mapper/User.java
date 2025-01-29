package com.example.demo.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: Entityは値の設定を行うために可変の@Dataとする

/**
 * ユーザエンティティ。
 */
@AllArgsConstructor
@Data
public class User {

    /** ユーザID。 */
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
