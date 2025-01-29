package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.response.SuccessResponse;
import com.example.demo.service.DepartmentResult;
import com.example.demo.service.DepartmentService;

import lombok.RequiredArgsConstructor;

// NOTE: Controllerはリクエスト、レスポンスの処理を書く
// NOTE:SpringBootではフレームワークでリクエスト、レスポンスの処理を行ってくれるので書くことがない
// NOTE: Serviceの呼び出しとServiceへ渡す値の変換処理だけ書く
// NOTE: ロジックは書かない

/**
 * 部署コントローラ。
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/departments")
public class DepartmentRestController {

    // NOTE: @Autowiredするクラスはprivate finalで定義し、@RequiredArgsConstructorでインジェクションする
    // NOTE: 書き換え不能にすることで不用意な書き換えを防ぐ

    /** 部署コントローラコンバーター。 */
    private final DepartmentRestControllerConverter converter;

    // NOTE: コントローラの単体テストはJSONが想定通りか確認するテストになる想定。そのためモックにする。

    /** 部署サービス。 */
    private final DepartmentService service;

    // NOTE: メソッド名はログ出力に表示させることが多いのでControllerとServiceのメソッド名は別々にする。また、どのAPIが呼び出されたかもログ出力で分かりやすいようにするため、APIごとの名前も別々にする

    /**
     * すべての部署レスポンスを取得する。
     * 
     * @param operator 操作者
     * @return 正常終了時のレスポンス（部署レスポンス）
     */
    @GetMapping
    public SuccessResponse fetchAllApi(
            @RequestHeader(name = "X-Operator", required = true) String operator) {
        DepartmentResult result = service.fetchAll(operator);

        DepartmentResponse response = converter.convertToResponse(result);

        return new SuccessResponse(response);
    }

}
