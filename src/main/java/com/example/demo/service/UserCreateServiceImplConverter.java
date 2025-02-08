package com.example.demo.service;

import org.springframework.stereotype.Component;

import com.example.demo.logic.GenerateIdLogic;
import com.example.demo.mapper.User;

import lombok.RequiredArgsConstructor;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ユーザ作成サービスコンバーター。
 */
@RequiredArgsConstructor
@Component
public class UserCreateServiceImplConverter {

    /** IDのフォーマット。 */
    private static final String FORMAT = "%s_%02d";

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ID生成ロジック。 */
    private final GenerateIdLogic generateIdLogic;

    /**
     * ユーザ作成パラメータをユーザエンティティに変換する。
     * 
     * @param param ユーザ作成パラメータ
     * @return ユーザエンティティ
     */
    public User convertToEntity(UserCreateParam param) {
        String id = generateIdLogic.generateId();

        User entity = new User(
                String.format(FORMAT, id, 1),
                param.getFamilyName(),
                param.getFirstName(),
                param.getDeptId(),
                0);
        return entity;
    }

}
