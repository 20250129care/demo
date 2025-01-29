package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.OperationConstants;
import com.example.demo.logic.LoggingLogic;
import com.example.demo.mapper.Department;
import com.example.demo.mapper.DepartmentMapper;

import lombok.RequiredArgsConstructor;

// NOTE: 業務の処理の流れを書く
// NOTE: 詳細設計書の各項目に書いてありそうなことを書くイメージ
// NOTE: 詳細設計書の各項目の詳細な記述は各メソッドに切り出しているイメージ

/**
 * 部署サービス実装。
 */
@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** 部署サービスコンバーター。 */
    private final DepartmentServiceImplConverter converter;

    /** 部署エンティティマッパー。 */
    private final DepartmentMapper departmentMapper;

    /** ロギングロジック。 */
    private final LoggingLogic loggingLogic;

    // NOTE: 処理ごとにログを出したい場合など
    // NOTE: logger.debugでデバッグ時表示
    // NOTE: logger.infoで常時表示
    // NOTE: logger.debug、logger.infoをprivateメソッドに切り出してしまうとログを実行した行数の情報がprivateメソッドの方になってしまうので直接使う

    /** ロガー。 */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public DepartmentResult fetchAll(String operator) {
        // 部署をすべて取得する
        logger.debug("部署をすべて取得する");
        List<Department> entityList = departmentMapper.findAll();

        // 部署結果を作成する
        logger.debug("部署結果を作成する");
        DepartmentResult result = converter.convertToResult(entityList);

        // 外部APIで操作ログを保存する
        logger.debug("外部APIで操作ログを保存する");
        loggingLogic.logOperation(OperationConstants.DEPARTMENT_FETCH_ALL, operator);

        return result;
    }

}
