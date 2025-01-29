package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.ApplicationException;
import com.example.demo.constant.OperationConstants;
import com.example.demo.logic.ExceptionCreator;
import com.example.demo.logic.LoggingLogic;
import com.example.demo.mapper.DepartmentMapper;
import com.example.demo.mapper.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserSummaryMapper;

import lombok.RequiredArgsConstructor;

// NOTE: 業務の処理の流れを書く
// NOTE: 詳細設計書の各項目に書いてありそうなことを書くイメージ
// NOTE: 詳細設計書の各項目の詳細な記述は各メソッドに切り出しているイメージ

/**
 * ユーザ作成サービス実装。
 */
@RequiredArgsConstructor
@Service
public class UserCreateServiceImpl implements UserCreateService {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ユーザ作成サービスコンバーター。 */
    private final UserCreateServiceImplConverter converter;

    /** 部署エンティティマッパー。 */
    private final DepartmentMapper departmentMapper;

    /** ユーザエンティティマッパー。 */
    private final UserMapper userMapper;

    /** ユーザ概要エンティティマッパー。 */
    private final UserSummaryMapper userSummaryMapper;

    /** 業務エラー作成。 */
    private final ExceptionCreator exceptionCreator;

    /** ロギングロジック。 */
    private final LoggingLogic externalApiLogic;

    // NOTE: 処理ごとにログを出したい場合など
    // NOTE: logger.debugでデバッグ時表示
    // NOTE: logger.infoで常時表示
    // NOTE: logger.debug、logger.infoをprivateメソッドに切り出してしまうとログを実行した行数の情報がprivateメソッドの方になってしまうので直接使う

    /** ロガー。 */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // NOTE: rollbackForでどんな例外が発生してもロールバックするように設定

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(String operator, UserCreateParam param) throws ApplicationException {
        // 登録するエンティティを作成する
        logger.debug("登録するエンティティを作成する");
        User entity = converter.convertToEntity(param);

        // 部署IDを取得する
        String deptId = entity.getDeptId();

        // 登録に使用する部署IDが存在することを確認する
        logger.debug("登録に使用する部署IDが存在することを確認する");
        boolean existsDept = departmentMapper.existsById(deptId);
        if (!existsDept) {
            throw exceptionCreator.create("401", deptId(entity));
        }

        // ユーザIDを取得する
        String userId = entity.getId();

        // 登録対象のデータが存在しないことを確認する
        logger.debug("登録対象のデータが存在しないことを確認する");
        boolean existsUser = userMapper.existsById(userId);
        if (existsUser) {
            throw exceptionCreator.create("101", userId(entity));
        }

        // テーブルに登録を行い、登録が成功しているか確認する
        logger.debug("テーブルに登録を行い、登録が成功しているか確認する");
        int insertedCount = userMapper.insert(entity);
        if (insertedCount != 1) {
            throw exceptionCreator.create("201", userId(entity));
        }

        // サマリーテーブルにコピーし、コピーが成功しているか確認する
        logger.debug("サマリーテーブルにコピーし、コピーが成功しているか確認する");
        int copiedCount = userSummaryMapper.copyFromUser(userId);
        if (copiedCount != 1) {
            throw exceptionCreator.create("301", userId(entity));
        }

        // 外部APIで操作ログを保存する
        logger.debug("外部APIで操作ログを保存する");
        externalApiLogic.logOperation(OperationConstants.USER_CREATE, operator);
    }

    // NOTE: メインの業務処理外で共通の内容はprivateメソッドに切り出す

    /**
     * ユーザIDを返却する。
     * 
     * @param entity ユーザエンティティ
     * @return ユーザID
     */
    private String userId(User entity) {
        String id = entity.getId();
        return id;
    }

    /**
     * 部署IDを返却する。
     * 
     * @param entity 部署エンティティ
     * @return 部署ID
     */
    private String deptId(User entity) {
        String id = entity.getDeptId();
        return id;
    }

}
