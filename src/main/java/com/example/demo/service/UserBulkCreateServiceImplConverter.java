package com.example.demo.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.example.demo.logic.GenerateIdLogic;
import com.example.demo.mapper.User;

import lombok.RequiredArgsConstructor;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * 複数のユーザ作成サービスコンバーター。
 */
@RequiredArgsConstructor
@Component
public class UserBulkCreateServiceImplConverter {

    /** IDのフォーマット。 */
    private final static String FORMAT = "%s_%02d";

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ID生成ロジック。 */
    private final GenerateIdLogic generateIdLogic;

    /**
     * 複数のユーザ作成パラメータをユーザエンティティリストに変換する。
     * 
     * @param bulkParam 複数のユーザ作成パラメータ
     * @return ユーザエンティティリスト
     */
    public List<User> convertToEntity(UserBulkCreateParam bulkParam) {
        String id = generateIdLogic.generateId();

        List<UserCreateParam> paramList = bulkParam.getList();

        List<User> entityList = IntStream.range(0, paramList.size())
                .mapToObj(i -> {
                    UserCreateParam param = paramList.get(i);
                    User entity = new User(
                            String.format(FORMAT, id, i + 1),
                            param.getFamilyName(),
                            param.getFirstName(),
                            param.getDeptId(),
                            0);
                    return entity;
                })
                .toList();
        return entityList;
    }

}
