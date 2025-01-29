package com.example.demo.common.exception;

/**
 * 業務エラー。
 */
public class ApplicationException extends Exception {

    /** Exceptionに設定するメッセージ。 */
    private static final String MESSAGE = "業務処理例外";

    /** エラーID。 */
    private final String errorId;

    /** エラーメッセージ。 */
    private final String errorMessage;

    /**
     * コンストラクタ。
     * 
     * @param errorId 設定するエラーID
     * @param errorMessage 設定するエラーメッセージ
     */
    public ApplicationException(String errorId, String errorMessage) {
        super(MESSAGE);

        this.errorId = errorId;
        this.errorMessage = errorMessage;
    }

    /**
     * エラーIDを取得する。
     * 
     * @return エラーID
     */
    public String getErrorId() {
        return errorId;
    }

    /**
     * エラーメッセージを取得する。
     * 
     * @return エラーメッセージ
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
