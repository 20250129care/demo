package com.example.demo.service;

import java.util.List;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * 部署結果。
 */
@Value
public class DepartmentResult {

    private List<DepartmentResultData> list;

}
