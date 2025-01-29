package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.service.UserBulkCreateParam;
import com.example.demo.service.UserCreateParam;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * 複数のユーザ作成コントローラコンバーター。
 */
@Component
public class UserBulkCreateRestControllerConverter {

    /**
     * 複数のユーザ作成リクエストを複数のユーザ作成パラメータに変換する。
     * 
     * @param bulkRequest 複数のユーザ作成リクエスト
     * @return 複数のユーザ作成パラメータ
     */
    public UserBulkCreateParam convertToParam(UserBulkCreateRequest bulkRequest) {
        List<UserCreateParam> list = bulkRequest.getList()
                .stream()
                .map(request -> {
                    UserCreateParam param = new UserCreateParam(
                            request.getFamilyName(),
                            request.getFirstName(),
                            request.getDeptId());
                    return param;
                })
                .toList();

        UserBulkCreateParam bulkParam = new UserBulkCreateParam(list);

        return bulkParam;
    }

}
