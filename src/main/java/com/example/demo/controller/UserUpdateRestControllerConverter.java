package com.example.demo.controller;

import org.springframework.stereotype.Component;

import com.example.demo.service.UserUpdateParam;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ユーザ更新コントローラコンバーター。
 */
@Component
public class UserUpdateRestControllerConverter {

    /**
     * ユーザ更新リクエストをユーザ更新パラメータに変換する。
     * 
     * @param request ユーザ更新リクエスト
     * @return ユーザ更新パラメータ
     */
    public UserUpdateParam convertToParam(UserUpdateRequest request) {
        UserUpdateParam param = new UserUpdateParam(
                request.getId(),
                request.getFamilyName(),
                request.getFirstName(),
                request.getDeptId(),
                request.getVersion());
        return param;
    }

}
