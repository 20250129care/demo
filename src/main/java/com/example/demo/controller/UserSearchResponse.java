package com.example.demo.controller;

import java.util.List;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ検索レスポンス。
 */
@Value
public class UserSearchResponse {

    /** データリスト。 */
    private List<UserSearchResponseData> list;

    /** 前のページのページ番号。前のページがなければNULL。 */
    private Integer prevPageNo;

    /** 次のページのページ番号。次のページがなければNULL。 */
    private Integer nextPageNo;

    /** 1ページで表示する件数。最大100件。 */
    private Integer pageSize;

}
