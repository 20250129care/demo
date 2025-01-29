package com.example.demo.logic;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.example.demo.constant.URLConstants;
import com.example.demo.external.ExternalApi;

import lombok.RequiredArgsConstructor;

// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ロギングロジック。
 */
@RequiredArgsConstructor
@Component
public class LoggingLogic {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ロギングロジックコンバーター。 */
    private final LoggingLogicConverter converter;

    /** メッセージソース。 */
    private final MessageSource messageSource;

    /** 外部API呼び出し処理。 */
    private final ExternalApi externalApi;

    /** ロガー。 */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 操作ログを保存する。例外はthrowsされないようにしている。
     * 
     * @param operation 操作内容
     * @param operator 操作者
     */
    public void logOperation(String operation, String operator) {
        Map<String, Object> body = converter.convertToBody(operation, operator);

        try {
            externalApi.post(URLConstants.LOGGING_URL, body);

            String message = messageSource.getMessage("I01", null, Locale.JAPAN);
            logger.info(message);
        } catch (HttpClientErrorException ex) {
            String message = messageSource.getMessage("W01", null, Locale.JAPAN);
            logger.warn(message, ex);
        } catch (HttpServerErrorException ex) {
            String message = messageSource.getMessage("W02", null, Locale.JAPAN);
            logger.warn(message, ex);
        } catch (ResourceAccessException ex) {
            String message = messageSource.getMessage("W03", null, Locale.JAPAN);
            logger.warn(message, ex);
        } catch (RuntimeException ex) {
            String message = messageSource.getMessage("W99", null, Locale.JAPAN);
            logger.warn(message, ex);
        }
    }

}
