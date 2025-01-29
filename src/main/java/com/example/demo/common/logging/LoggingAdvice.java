package com.example.demo.common.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// NOTE: commonのクラスは開発者は触らないイメージ

/**
 * ControllerとServiceの各メソッドの呼び出しをログに出力する。
 */
@Aspect
@Component
public final class LoggingAdvice {

    /** 処理開始。 */
    private static final String BEGIN = "[処理開始]";

    /** 処理終了。 */
    private static final String END = "[処理終了]";

    /** 区切り文字。 */
    private static final String SEP = ".";

    /** ロガー。 */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * コントローラのメソッド開始時にログ出力する。
     * 
     * @param jp JoinPoint
     */
    @Before("execution(* com.example.demo.controller.*Controller.*(..))")
    public void beforeMethodOfController(JoinPoint jp) {
        String className = jp.getTarget().getClass().toString();
        String signatureName = jp.getSignature().getName();
        logger.info(BEGIN + className + SEP + signatureName);
    }

    /**
     * サービスのメソッド開始時にログ出力する。
     * 
     * @param jp JoinPoint
     */
    @Before("execution(* com.example.demo.service.*ServiceImpl.*(..))")
    public void beforeMethodOfService(JoinPoint jp) {
        String className = jp.getTarget().getClass().toString();
        String signatureName = jp.getSignature().getName();
        logger.info(BEGIN + className + SEP + signatureName);
    }

    /**
     * コントローラのメソッド終了時にログ出力する。
     * 
     * @param jp JoinPoint
     */
    @AfterReturning("execution(* com.example.demo.controller.*Controller.*(..))")
    public void afterMethodOfController(JoinPoint jp) {
        String className = jp.getTarget().getClass().toString();
        String signatureName = jp.getSignature().getName();
        logger.info(END + className + SEP + signatureName);
    }

    /**
     * サービスのメソッド終了時にログ出力する。
     * 
     * @param jp JoinPoint
     */
    @AfterReturning("execution(* com.example.demo.service.*ServiceImpl.*(..))")
    public void afterMethodOfService(JoinPoint jp) {
        String className = jp.getTarget().getClass().toString();
        String signatureName = jp.getSignature().getName();
        logger.info(END + className + SEP + signatureName);
    }

}
