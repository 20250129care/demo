package com.example.demo.controller;

import java.util.List;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * 部署レスポンス。
 */
@Value
public class DepartmentResponse {

    /** データリスト。 */
    private List<DepartmentResponseData> list;

}
