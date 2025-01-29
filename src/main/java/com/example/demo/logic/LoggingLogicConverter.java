package com.example.demo.logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.constant.DateFormatConstants;

// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ロギングロジックコンバーター。
 */
@Component
public class LoggingLogicConverter {

    /** 日時フォーマッター。 */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormatConstants.REQUEST_PATTERN);

    /**
     * 引数からボディに変換する。
     * 
     * @param operation 操作内容
     * @param operator 操作者
     * @return ボディ
     */
    public Map<String, Object> convertToBody(String operation, String operator) {
        String operationDate = LocalDateTime.now().format(formatter);

        Map<String, Object> body = new HashMap<>();
        body.put("operation", operation);
        body.put("operator", operator);
        body.put("operation_date", operationDate);
        return body;
    }

}
