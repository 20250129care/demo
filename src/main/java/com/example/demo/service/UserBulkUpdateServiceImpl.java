package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

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
 * 複数のユーザ更新サービス実装。
 */
@RequiredArgsConstructor
@Service
public class UserBulkUpdateServiceImpl implements UserBulkUpdateService {

    /** 区切り文字。 */
    private static final String SEP = ",";

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** 複数のユーザ更新サービスコンバーター。 */
    private final UserBulkUpdateServiceImplConverter converter;

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
    public void bulkUpdate(String operator, UserBulkUpdateParam bulkParam) throws ApplicationException {
        // 更新するエンティティを作成する
        logger.debug("更新するエンティティを作成する");
        List<User> entityList = converter.convertToEntity(bulkParam);

        // 重複のない部署IDリストを取得する
        List<String> deptIdList = entityList.stream().map(User::getDeptId).distinct().toList();

        // 登録する部署IDが存在することを確認する
        logger.debug("登録する部署IDが存在することを確認する");
        boolean existsDept = departmentMapper.existsByIdList(deptIdList);
        if (!existsDept) {
            throw exceptionCreator.create("402", deptIds(entityList));
        }

        // 重複のないユーザIDリストを取得する
        List<String> userIdList = entityList.stream().map(User::getId).distinct().toList();

        // 更新対象のデータが存在することを確認する
        logger.debug("更新対象のデータが存在することを確認する");
        boolean existsUserList = userMapper.existsByIdList(userIdList);
        if (!existsUserList) {
            throw exceptionCreator.create("102", userIds(entityList));
        }

        // テーブルの更新を行い、更新が成功しているか確認する
        logger.debug("テーブルの更新を行い、更新が成功しているか確認する");
        int updatedCount = userMapper.updateList(entityList);
        if (updatedCount != entityList.size()) {
            throw exceptionCreator.create("202", userIds(entityList));
        }

        // サマリーテーブルを更新し、更新が成功しているか確認する
        logger.debug("サマリーテーブルを更新し、更新が成功しているか確認する");
        int modifiedCount = userSummaryMapper.modifyListFromUser(userIdList);
        if (modifiedCount != entityList.size()) {
            throw exceptionCreator.create("302", userIds(entityList));
        }

        // 外部APIで操作ログを保存する
        logger.debug("外部APIで操作ログを保存する");
        externalApiLogic.logOperation(OperationConstants.USER_BULK_UPDATE, operator);
    }

    // NOTE: メインの業務処理外で共通の内容はprivateメソッドに切り出す

    /**
     * カンマ区切りのユーザIDリストを返却する。
     * 
     * @param entityList ユーザエンティティリスト
     * @return カンマ区切りのユーザIDリスト
     */
    private String userIds(List<User> entityList) {
        String ids = entityList
                .stream()
                .map(User::getId)
                .collect(Collectors.joining(SEP));
        return ids;
    }

    /**
     * カンマ区切りの部署IDリストを返却する。
     * 
     * @param entityList 部署エンティティリスト
     * @return カンマ区切りの部署IDリスト
     */
    private String deptIds(List<User> entityList) {
        String ids = entityList
                .stream()
                .map(User::getDeptId)
                .collect(Collectors.joining(SEP));
        return ids;
    }

}