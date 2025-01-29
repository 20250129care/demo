package com.example.demo.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: Entityは値の設定を行うために可変の@Dataとする

/**
 * 部署エンティティ。
 */
@AllArgsConstructor
@Data
public class Department {

    /** 部署ID。 */
    private String id;

    /** 部署名。 */
    private String name;

    /** 削除済み。 */
    private Boolean isDeleted;

}
