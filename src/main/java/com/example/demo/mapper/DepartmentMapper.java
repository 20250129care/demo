package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 部署エンティティマッパー。
 */
@Mapper
public interface DepartmentMapper {

    /**
     * 対象の部署IDが存在するかどうか。
     * 
     * @param id 部署ID
     * @return 存在する: true / 存在しない: false
     */
    boolean existsById(String id);

    /**
     * 対象の部署IDがすべて存在するかどうか。
     * 
     * @param idList 部署IDリスト
     * @return すべて存在する: true / 1つでも存在しない: false
     */
    boolean existsByIdList(List<String> idList);

    /**
     * すべての部署エンティティを取得する。
     * 
     * @return 部署エンティティリスト
     */
    List<Department> findAll();

}
