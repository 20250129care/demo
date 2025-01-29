package com.example.demo.service;

import org.springframework.stereotype.Component;

import com.example.demo.mapper.User;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ユーザ更新サービスコンバーター。
 */
@Component
public class UserUpdateServiceImplConverter {

    /**
     * ユーザ更新パラメータをユーザエンティティに変換する。
     * 
     * @param param ユーザ更新パラメータ
     * @return ユーザエンティティ
     */
    public User convertToEntity(UserUpdateParam param) {
        User entity = new User(
                param.getId(),
                param.getFamilyName(),
                param.getFirstName(),
                param.getDeptId(),
                param.getVersion());
        return entity;
    }

}
