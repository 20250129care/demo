package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * ユーザエンティティマッパー。
 */
@Mapper
public interface UserMapper {

    /**
     * 対象のユーザIDが存在するかどうか。
     * 
     * @param id ユーザID
     * @return 存在する: true / 存在しない: false
     */
    boolean existsById(String id);

    /**
     * 対象のユーザIDがすべて存在するかどうか。
     * 
     * @param idList ユーザIDリスト
     * @return すべて存在する: true / 1つでも存在しない: false
     */
    boolean existsByIdList(List<String> idList);

    /**
     * 対象のユーザエンティティを更新する。
     * 
     * @param entity 更新するユーザエンティティ
     * @return 更新成功件数
     */
    int update(User entity);

    /**
     * 対象のユーザエンティティをすべて更新する。
     * 
     * @param entityList 更新するユーザエンティティリスト
     * @return 更新成功件数
     */
    int updateList(List<User> entityList);

    /**
     * 対象のユーザエンティティを登録する。
     * 
     * @param entity 登録するユーザエンティティ
     * @return 登録成功件数
     */
    int insert(User entity);

    /**
     * 対象のユーザエンティティをすべて登録する。
     * 
     * @param entityList 登録するユーザエンティティリスト
     * @return 登録成功件数
     */
    int insertList(List<User> entityList);

}