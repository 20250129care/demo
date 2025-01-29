package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.service.UserBulkUpdateParam;
import com.example.demo.service.UserUpdateParam;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * 複数ユーザ更新コントローラコンバーター。
 */
@Component
public class UserBulkUpdateRestControllerConverter {

    /**
     * 複数のユーザ更新リクエストを複数のユーザ更新パラメータに変換する。
     * 
     * @param bulkRequest 複数のユーザ更新リクエスト
     * @return 複数のユーザ更新パラメータ
     */
    public UserBulkUpdateParam convertToParam(UserBulkUpdateRequest bulkRequest) {
        List<UserUpdateParam> list = bulkRequest.getList()
                .stream()
                .map(request -> {
                    UserUpdateParam param = new UserUpdateParam(
                            request.getId(),
                            request.getFamilyName(),
                            request.getFirstName(),
                            request.getDeptId(),
                            request.getVersion());
                    return param;
                })
                .toList();

        UserBulkUpdateParam bulkParam = new UserBulkUpdateParam(list);

        return bulkParam;
    }

}
