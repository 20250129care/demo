package com.example.demo.service;

import java.util.List;

import com.example.demo.mapper.User;
import org.springframework.stereotype.Component;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * 複数のユーザ更新サービスコンバーター。
 */
@Component
public class UserBulkUpdateServiceImplConverter {

    /**
     * 複数のユーザ更新パラメータをユーザエンティティリストに変換する。
     * 
     * @param bulkParam 複数のユーザ更新パラメータ
     * @return ユーザエンティティリスト
     */
    public List<User> convertToEntity(UserBulkUpdateParam bulkParam) {
        List<User> entityList = bulkParam.getList()
                .stream()
                .map(param -> {
                    User entity = new User(
                            param.getId(),
                            param.getFamilyName(),
                            param.getFirstName(),
                            param.getDeptId(),
                            param.getVersion());
                    return entity;
                })
                .toList();
        return entityList;
    }

}
