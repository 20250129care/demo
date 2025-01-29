package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.service.UserSearchParam;
import com.example.demo.service.UserSearchResult;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ユーザ検索コントローラコンバーター。
 */
@Component
public class UserSearchRestControllerConverter {

    /**
     * ユーザ検索リクエストをユーザ検索パラメータに変換する。
     * 
     * @param request ユーザ検索リクエスト
     * @return ユーザ検索パラメータ
     */
    UserSearchParam convertToParam(UserSearchRequest request) {
        UserSearchParam param = new UserSearchParam(
                request.getName(),
                request.getDeptId(),
                request.getBeginUpdatedAt(),
                request.getEndUpdatedAt());
        return param;
    }

    /**
     * ユーザ検索結果をユーザ検索レスポンスに変換する。
     * 
     * @param result ユーザ検索結果
     * @return ユーザ検索レスポンス
     */
    UserSearchResponse convertToResponse(UserSearchResult result) {
        List<UserSearchResponseData> responseList = result.getList()
                .stream()
                .map(data -> {
                    UserSearchResponseData response = new UserSearchResponseData(
                            data.getName(),
                            data.getDeptId(),
                            data.getDeptName(),
                            data.getLastUpdatedAt(),
                            data.getUserId(),
                            data.getUserVersion());
                    return response;
                })
                .toList();

        UserSearchResponse response = new UserSearchResponse(responseList);

        return response;
    }

}
