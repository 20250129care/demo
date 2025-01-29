package com.example.demo.logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.demo.constant.DateFormatConstants;

// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ID生成ロジック。
 */
@Component
public class GenerateIdLogic {

    /** 日時フォーマッター。 */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormatConstants.ID_PATTERN);

    /**
     * IDを生成する。
     * 
     * @return ID
     */
    public String generateId() {
        String id = LocalDateTime.now().format(formatter);
        return id;
    }

}
