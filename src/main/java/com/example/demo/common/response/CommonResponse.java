package com.example.demo.common.response;

import lombok.Data;

// NOTE: commonのクラスは開発者は触らないイメージ
// NOTE: classにpublicをつけないことで直接呼び出せないようにしている

/**
 * 共通のレスポンス。
 */
@Data
class CommonResponse {

    /** ステータス。 */
    private final String status;

    /** コード。 */
    private final String code;

    /** メッセージ。 */
    private final String message;

    /** データ。 */
    private final Object data;

}
