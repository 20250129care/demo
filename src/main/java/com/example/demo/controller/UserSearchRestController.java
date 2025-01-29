package com.example.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.response.SuccessResponse;
import com.example.demo.service.UserSearchParam;
import com.example.demo.service.UserSearchResult;
import com.example.demo.service.UserSearchService;

import lombok.RequiredArgsConstructor;

// NOTE: Controllerはリクエスト、レスポンスの処理を書く
// NOTE: SpringBootではフレームワークでリクエスト、レスポンスの処理を行ってくれるので書くことがない
// NOTE: Serviceの呼び出しとServiceへ渡す値の変換処理だけ書く
// NOTE: ロジックは書かない

/**
 * ユーザ検索コントローラ。
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserSearchRestController {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ユーザ検索コントローラコンバーター。 */
    private final UserSearchRestControllerConverter converter;

    // NOTE: コントローラの単体テストはJSONが想定通りか確認するテストになる想定。そのためモックにする。

    /** ユーザ検索サービス。 */
    private final UserSearchService service;

    // NOTE: メソッド名はログ出力に表示させることが多いのでControllerとServiceのメソッド名は別々にする。また、どのAPIが呼び出されたかもログ出力で分かりやすいようにするため、APIごとの名前も別々にする

    /**
     * ユーザを検索する。
     * 
     * @param operator 操作者
     * @param request ユーザ検索リクエスト
     * @return 正常終了時のレスポンス（ユーザ検索レスポンス）
     */
    @PostMapping("/search")
    public SuccessResponse searchApi(
            @RequestHeader(name = "X-Operator", required = true) String operator,
            @Validated @RequestBody(required = true) UserSearchRequest request) {
        UserSearchParam param = converter.convertToParam(request);

        UserSearchResult result = service.search(operator, param);

        UserSearchResponse response = converter.convertToResponse(result);

        return new SuccessResponse(response);
    }

}
