package com.example.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.exception.ApplicationException;
import com.example.demo.common.response.SuccessResponse;
import com.example.demo.service.UserUpdateParam;
import com.example.demo.service.UserUpdateService;

import lombok.RequiredArgsConstructor;

// NOTE: Controllerはリクエスト、レスポンスの処理を書く
// NOTE: SpringBootではフレームワークでリクエスト、レスポンスの処理を行ってくれるので書くことがない
// NOTE: Serviceの呼び出しとServiceへ渡す値の変換処理だけ書く
// NOTE: ロジックは書かない

/**
 * ユーザ更新コントローラ。
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserUpdateRestController {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** ユーザ更新コントローラコンバーター。 */
    private final UserUpdateRestControllerConverter converter;

    // NOTE: コントローラの単体テストはJSONが想定通りか確認するテストになる想定。そのためモックにする。

    /** ユーザ更新サービス。 */
    private final UserUpdateService service;

    // NOTE: メソッド名はログ出力に表示させることが多いのでControllerとServiceのメソッド名は別々にする。また、どのAPIが呼び出されたかもログ出力で分かりやすいようにするため、APIごとの名前も別々にする

    /**
     * ユーザを更新する。
     * 
     * @param operator 操作者
     * @param request ユーザ更新リクエスト
     * @return 正常終了時のレスポンス（データなし）
     * @throws ApplicationException 業務エラー
     */
    @PostMapping("/update")
    public SuccessResponse updateApi(
            @RequestHeader(name = "X-Operator", required = true) String operator,
            @Validated @RequestBody(required = true) UserUpdateRequest request) throws ApplicationException {
        UserUpdateParam param = converter.convertToParam(request);

        service.update(operator, param);

        return new SuccessResponse();
    }

}
