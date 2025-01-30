package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.mapper.UserSummary;
import com.example.demo.mapper.UserSummaryCondition;

// NOTE: DTOの変換処理はよく手が入るため切り出しておく
// NOTE: 実装や単体テストの修正がやりやすくなる
// NOTE: どのクラスで使う変換処理なのかわかりやすいように対象のクラス名を含んだクラス名にしている
// NOTE: Controller, Service, Mapper以外はすべてComponentとする

/**
 * ユーザ検索サービスコンバーター。
 */
@Component
public class UserSearchServiceImplConverter {

    /**
     * ユーザ検索パラメータをユーザ概要の検索条件に変換する。
     * 
     * @param param ユーザ検索パラメータ
     * @return ユーザ概要の検索条件
     */
    public UserSummaryCondition convertToCondition(UserSearchParam param) {
        Integer limit = param.getPageSize() + 1;
        Integer offset = (param.getPageNo() - 1) * param.getPageSize();

        UserSummaryCondition condition = new UserSummaryCondition(
                param.getName(),
                param.getDeptId(),
                param.getBeginUpdatedAt(),
                param.getEndUpdatedAt(),
                limit,
                offset);
        return condition;
    }

    /**
     * エンティティリストをユーザ検索結果に変換する。
     * 
     * @param entityList ユーザ概要エンティティリスト
     * @param pageNo ページ番号
     * @param pageSize 1ページで表示する件数
     * @return ユーザ検索結果
     */
    public UserSearchResult convertToResult(List<UserSummary> entityList, Integer pageNo, Integer pageSize) {
        Integer prevPageNo = pageNo > 1 ? pageNo - 1 : null;
        Integer nextPageNo = entityList.size() > pageSize ? pageNo + 1 : null;

        List<UserSearchResultData> list = entityList
                .stream()
                .limit(pageSize)
                .map(entity -> {
                    UserSearchResultData result = new UserSearchResultData(
                            entity.getName(),
                            entity.getDeptId(),
                            entity.getDeptName(),
                            entity.getLastUpdatedAt(),
                            entity.getUserId(),
                            entity.getUserVersion());
                    return result;
                })
                .toList();

        UserSearchResult result = new UserSearchResult(
                list,
                prevPageNo,
                nextPageNo,
                pageSize);

        return result;
    }

}
