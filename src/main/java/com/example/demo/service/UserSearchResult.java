package com.example.demo.service;

import java.util.List;

import lombok.Value;

// NOTE: コンストラクタで各フィールドを設定することで漏れをなくしたい
// NOTE: 不変性を保ちたいため@Value

/**
 * ユーザ検索結果。
 */
@Value
public class UserSearchResult {

    /** データリスト。 */
    private List<UserSearchResultData> list;

    /** 前のページのページ番号。前のページがなければNULL。 */
    private Integer prevPageNo;

    /** 次のページのページ番号。次のページがなければNULL。 */
    private Integer nextPageNo;

    /** １ページで表示する件数。最大100件。 */
    private Integer pageSize;

}
