package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.OperationConstants;
import com.example.demo.logic.LoggingLogic;
import com.example.demo.mapper.UserSummary;
import com.example.demo.mapper.UserSummaryCondition;
import com.example.demo.mapper.UserSummaryMapper;

import lombok.RequiredArgsConstructor;

// NOTE: 業務の処理の流れを書く
// NOTE: 詳細設計書の各項目に書いてありそうなことを書くイメージ
// NOTE: 詳細設計書の各項目の詳細な記述は各メソッドに切り出しているイメージ

/**
 * ユーザ検索サービス実装。
 */
@RequiredArgsConstructor
@Service
public class UserSearchServiceImpl implements UserSearchService {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ユーザ検索サービスコンバーター。 */
    private final UserSearchServiceImplConverter converter;

    /** ユーザ概要エンティティマッパー。　 */
    private final UserSummaryMapper userSummaryMapper;

    /** ロギングロジック。 */
    private final LoggingLogic externalApiLogic;

    // NOTE: 処理ごとにログを出したい場合など
    // NOTE: logger.debugでデバッグ時表示
    // NOTE: logger.infoで常時表示
    // NOTE: logger.debug、logger.infoをprivateメソッドに切り出してしまうとログを実行した行数の情報がprivateメソッドの方になってしまうので直接使う

    /** ロガー。 */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // NOTE: rollbackForでどんな例外が発生してもロールバックするように設定
    // NOTE: readOnlyで取得以外のDBアクセスが行われた場合に例外とする

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public UserSearchResult search(String operator, UserSearchParam param) {
        // 検索条件を作成する
        logger.debug("検索条件を作成する");
        UserSummaryCondition condition = converter.convertToCondition(param);

        // 検索対象のデータを取得する
        logger.debug("検索対象のデータを取得する");
        List<UserSummary> entityList = userSummaryMapper.find(condition);

        // 検索結果を作成する
        logger.debug("検索結果を作成する");
        UserSearchResult result = converter.convertToResult(entityList);

        // 外部APIで操作ログを保存する
        logger.debug("外部APIで操作ログを保存する");
        externalApiLogic.logOperation(OperationConstants.USER_SEARCH, operator);

        return result;
    }

}