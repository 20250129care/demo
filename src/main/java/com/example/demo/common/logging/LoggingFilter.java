package com.example.demo.common.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

// NOTE: commonのクラスは開発者は触らないイメージ

/**
 * リクエストとレスポンスのJSONをログに出力する。
 */
@Component
public final class LoggingFilter extends OncePerRequestFilter {

    /** 出力フォーマット。 */
    private static final String FORMAT = "FINISHED PROCESSING : METHOD={}; REQUESTURI={}; QUERY STRING={}; REQUEST PAYLOAD={}; RESPONSE CODE={}; RESPONSE={}; TIM TAKEN={}";

    /** ロガー。 */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), "UTF8");
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), "UTF8");

        logger.info(FORMAT,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                requestBody,
                response.getStatus(),
                responseBody,
                timeTaken);

        responseWrapper.copyBodyToResponse();
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding)
            throws UnsupportedEncodingException {
        return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
    }

}