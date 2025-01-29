package com.example.demo.constant;

// NOTE: 定数クラスはインスタンス化できないようにprivateコンストラクタを定義する

/**
 * 操作内容定数。
 */
public class OperationConstants {

    public static final String USER_CREATE = "ユーザ登録";

    public static final String USER_UPDATE = "ユーザ更新";

    public static final String USER_SEARCH = "ユーザ検索";

    public static final String USER_BULK_CREATE = "ユーザ登録（複数）";

    public static final String USER_BULK_UPDATE = "ユーザ更新（複数）";

    public static final String DEPARTMENT_FETCH_ALL = "部署全件取得";

    /**
     * 非インスタンス化コンストラクタ。
     */
    private OperationConstants() {
    }

}
