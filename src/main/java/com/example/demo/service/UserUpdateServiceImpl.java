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
 * ユーザ更新サービス実装。
 */
@RequiredArgsConstructor
@Service
public class UserUpdateServiceImpl implements UserUpdateService {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ユーザ更新サービスコンバーター。 */
    private final UserUpdateServiceImplConverter converter;

    /** 部署エンティティマッパー。 */
    private final DepartmentMapper departmentMapper;

    /** ユーザエンティティマッパー。 */
    private final UserMapper userMapper;

    /** ユーザ概要エンティティマッパー。 */
    private final UserSummaryMapper userSummaryMapper;

    /** 業務エラー作成。 */
    private final ExceptionCreator exceptionCreator;

    /** ロギングロジック。 */
    private final LoggingLogic loggingLogic;

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
    public void update(String operator, UserUpdateParam param) throws ApplicationException {
        // 更新するエンティティを作成する
        logger.debug("更新するエンティティを作成する");
        User entity = converter.convertToEntity(param);

        // 部署IDを取得する
        String deptId = entity.getDeptId();

        // 更新に使用する部署IDが存在することを確認する
        logger.debug("更新に使用する部署IDが存在することを確認する");
        boolean existsDept = departmentMapper.existsById(deptId);
        if (!existsDept) {
            throw exceptionCreator.create("402", deptId(entity));
        }

        // ユーザIDを取得する
        String userId = entity.getId();

        // 更新対象のデータが存在することを確認する
        logger.debug("更新対象のデータが存在することを確認する");
        boolean existsUser = userMapper.existsById(userId);
        if (!existsUser) {
            throw exceptionCreator.create("102", userId(entity));
        }

        // テーブルの更新を行い、更新が成功しているか確認する
        logger.debug("テーブルの更新を行い、更新が成功しているか確認する");
        int updatedCount = userMapper.update(entity);
        if (updatedCount != 1) {
            throw exceptionCreator.create("202", userId(entity));
        }

        // サマリーテーブルを更新し、更新が成功しているか確認する
        logger.debug("サマリーテーブルを更新し、更新が成功しているか確認する");
        int modifiedCount = userSummaryMapper.modifyFromUser(userId);
        if (modifiedCount != 1) {
            throw exceptionCreator.create("302", userId(entity));
        }

        // 外部APIで操作ログを保存する
        logger.debug("外部APIで操作ログを保存する");
        loggingLogic.logOperation(OperationConstants.USER_UPDATE, operator);
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