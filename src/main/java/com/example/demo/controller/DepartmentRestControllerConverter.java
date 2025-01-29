package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.service.DepartmentResult;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * 部署コントローラコンバーター。
 */
@Component
public class DepartmentRestControllerConverter {

    /**
     * 部署結果リストを部署レスポンスリストに変換する。
     * 
     * @param result 部署結果
     * @return 部署レスポンス
     */
    DepartmentResponse convertToResponse(DepartmentResult result) {
        List<DepartmentResponseData> list = result.getList()
                .stream()
                .map(data -> {
                    DepartmentResponseData response = new DepartmentResponseData(
                            data.getId(),
                            data.getName(),
                            data.getIsDeleted());
                    return response;
                })
                .toList();

        DepartmentResponse response = new DepartmentResponse(list);

        return response;
    }

}
