package com.example.demo.constant;

// NOTE: 定数クラスはインスタンス化できないようにprivateコンストラクタを定義する

/**
 * 日時フォーマッター定数。
 */
public class DateFormatConstants {

    /** IDの日時。 */
    public static final String ID_PATTERN = "yyyyMMddHHmmssSSS";

    /** リクエストの日時。 */
    public static final String REQUEST_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 非インスタンス化コンストラクタ。
     */
    private DateFormatConstants() {
    }

}
