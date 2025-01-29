package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.mapper.Department;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * 部署サービスコンバーター。
 */
@Component
public class DepartmentServiceImplConverter {

    /**
     * 部署エンティティリストを部署結果リストに変換する。
     * 
     * @param entityList 部署エンティティリスト
     * @return 部署結果
     */
    DepartmentResult convertToResult(List<Department> entityList) {
        List<DepartmentResultData> list = entityList
                .stream()
                .map(entity -> {
                    DepartmentResultData result = new DepartmentResultData(
                            entity.getId(),
                            entity.getName(),
                            entity.getIsDeleted());
                    return result;
                })
                .toList();

        DepartmentResult result = new DepartmentResult(list);

        return result;
    }

}
