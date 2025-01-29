package com.example.demo.controller;

import org.springframework.stereotype.Component;

import com.example.demo.service.UserCreateParam;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ユーザ作成コントローラコンバーター。
 */
@Component
public class UserCreateRestControllerConverter {

    /**
     * ユーザ作成リクエストをユーザ作成パラメータに変換する。
     * 
     * @param request ユーザ作成リクエスト
     * @return ユーザ作成パラメータ
     */
    public UserCreateParam convertToParam(UserCreateRequest request) {
        UserCreateParam param = new UserCreateParam(
                request.getFamilyName(),
                request.getFirstName(),
                request.getDeptId());
        return param;
    }

}
