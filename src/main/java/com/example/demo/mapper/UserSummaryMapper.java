package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ユーザ概要エンティティマッパー。
 */
@Mapper
public interface UserSummaryMapper {

    /**
     * 検索条件に一致するユーザ概要エンティティを検索する。
     * 
     * @param condition ユーザ概要の検索条件
     * @return ユーザ概要エンティティリスト
     */
    List<UserSummary> find(UserSummaryCondition condition);

    /**
     * 対象のユーザエンティティからユーザ概要エンティティへレコードをコピーする。
     * 
     * @param id ユーザID
     * @return コピー成功件数
     */
    int copyFromUser(String id);

    /**
     * 対象のユーザエンティティからユーザ概要エンティティへレコードをすべてコピーする。
     * 
     * @param idList ユーザIDリスト
     * @return コピー成功件数
     */
    int copyListFromUser(List<String> idList);

    /**
     * 対象のユーザエンティティからユーザ概要エンティティへレコードを最新の状態へ変更する。
     * 
     * @param id ユーザID
     * @return 最新の状態へ変更成功件数
     */
    int modifyFromUser(@Param("id") String id);

    /**
     * 対象のユーザエンティティからユーザ概要エンティティへレコードをすべて最新の状態へ変更する。
     * 
     * @param idList ユーザIDリスト
     * @return 最新の状態へ変更成功件数
     */
    int modifyListFromUser(List<String> idList);

}