package com.example.demo.common.response;

import lombok.EqualsAndHashCode;
import lombok.ToString;

// NOTE: commonのクラスは開発者は触らないイメージ
// NOTE: callSuper = trueをつけることで継承元のクラスで定義されている変数も見られるようにする

/**
 * 異常終了時のレスポンス。
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends CommonResponse {

    /** レスポンスのステータス定数。 */
    private static final String STATUS = "error";

    /**
     * コンストラクタ。
     * 
     * @param code レスポンスに設定するコード
     * @param message レスポンスに設定するメッセージ
     */
    public ErrorResponse(String code, String message) {
        super(STATUS, code, message, null);
    }

    /**
     * コンストラクタ。
     * 
     * @param code レスポンスに設定するコード
     * @param message レスポンスに設定するメッセージ
     * @param data レスポンスに設定するデータ
     */
    public ErrorResponse(String code, String message, Object data) {
        super(STATUS, code, message, data);
    }

}
