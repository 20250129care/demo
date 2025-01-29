package com.example.demo.common.response;

import lombok.EqualsAndHashCode;
import lombok.ToString;

// NOTE: commonのクラスは開発者は触らないイメージ
// NOTE: callSuper = trueをつけることで継承元のクラスで定義されている変数も見られるようにする

/**
 * 正常終了時のレスポンス。
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SuccessResponse extends CommonResponse {

    /** レスポンスのステータス定数。 */
    private static final String STATUS = "success";

    /**
     * コンストラクタ。
     */
    public SuccessResponse() {
        super(STATUS, null, null, null);
    }

    /**
     * コンストラクタ。
     * 
     * @param data レスポンスに設定するデータ
     */
    public SuccessResponse(Object data) {
        super(STATUS, null, null, data);
    }

}
