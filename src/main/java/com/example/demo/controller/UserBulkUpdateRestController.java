package com.example.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.exception.ApplicationException;
import com.example.demo.common.response.SuccessResponse;
import com.example.demo.service.UserBulkUpdateParam;
import com.example.demo.service.UserBulkUpdateService;

import lombok.RequiredArgsConstructor;

// NOTE: Controllerはリクエスト、レスポンスの処理を書く
// NOTE: SpringBootではフレームワークでリクエスト、レスポンスの処理を行ってくれるので書くことがない
// NOTE: Serviceの呼び出しとServiceへ渡す値の変換処理だけ書く
// NOTE: ロジックは書かない

/**
 * 複数のユーザ更新コントローラ。
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserBulkUpdateRestController {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** 複数のユーザ更新コントローラコンバーター。 */
    private final UserBulkUpdateRestControllerConverter converter;

    // NOTE: コントローラの単体テストはJSONが想定通りか確認するテストになる想定。そのためモックにする。

    /** 複数のユーザ更新サービス。 */
    private final UserBulkUpdateService service;

    // NOTE: メソッド名はログ出力に表示させることが多いのでControllerとServiceのメソッド名は別々にする。また、どのAPIが呼び出されたかもログ出力で分かりやすいようにするため、APIごとの名前も別々にする

    /**
     * 複数のユーザを更新する。
     * 
     * @param operator 操作者
     * @param bulkRequest 複数のユーザ更新リクエスト
     * @return 正常終了時のレスポンス（データなし）
     * @throws ApplicationException 業務エラー
     */
    @PostMapping("/bulk/update")
    public SuccessResponse bulkUpdateApi(
            @RequestHeader(name = "X-Operator", required = true) String operator,
            @Validated @RequestBody(required = true) UserBulkUpdateRequest bulkRequest) throws ApplicationException {
        UserBulkUpdateParam param = converter.convertToParam(bulkRequest);

        service.bulkUpdate(operator, param);

        return new SuccessResponse();
    }

}
