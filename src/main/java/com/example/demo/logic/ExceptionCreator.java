package com.example.demo.logic;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.example.demo.common.exception.ApplicationException;

import lombok.RequiredArgsConstructor;

// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * 業務エラー作成。
 */
@RequiredArgsConstructor
@Component
public class ExceptionCreator {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE* 書き換え不能にすることで不用意な書き換えを防ぐ

    /** メッセージソース。 */
    private final MessageSource messageSource;

    /**
     * 業務エラーを作成する。
     * 
     * @param errorId エラーメッセージID
     * @param args エラーメッセージの引数
     * @return 業務エラー
     */
    public ApplicationException create(String errorId, Object... args) {
        String errorMessage = messageSource.getMessage(errorId, args, Locale.JAPAN);
        return new ApplicationException(errorId, errorMessage);
    }

}
