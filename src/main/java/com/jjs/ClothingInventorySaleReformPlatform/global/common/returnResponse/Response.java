package com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@Component
public class Response {

    @Getter
    @Builder
    private static class Body {

        private int state;
        private String result;
        private String massage;
        private Object data;
        private Object error;
    }
    @Getter
    @Builder
    private static class Body2 {

        private int state;
        private String result;
        private String massage;
        private Object error;
    }

    public ResponseEntity<?> success(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("success")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * <p> 메세지만 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : message,
     *         "data" : [],
     *         "error" : []
     *     }
     * </pre>
     *
     * @param msg 응답 바디 message 필드에 포함될 정보
     * @return 응답 객체
     *
     * 사용 예시 : return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
     */
    public ResponseEntity<?> success(String msg) {
        return success(Collections.emptyList(), msg, HttpStatus.OK);
    }

    public ResponseEntity<?> success(String msg, HttpStatus status) {
        Body2 body = Body2.builder()
                .state(status.value())
                .result("success")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * <p> 데이터만 가진 성공 응답을 반환한다.</p>
     * <pre>
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : null,
     *         "data" : [{data1}, {data2}...],
     *         "error" : []
     *     }
     * </pre>
     *
     * @param data 응답 바디 data 필드에 포함될 정보
     * @return 응답 객체
     *
     * 사용 예시 : return response.success("로그아웃 되었습니다.");
     */
    public ResponseEntity<?> success(Object data) {
        return success(data, null, HttpStatus.OK);
    }


    /**
     * <p> 성공 응답만 반환한다. </p>
     * <pre>
     *     {
     *         "state" : 200,
     *         "result" : success,
     *         "message" : null,
     *         "data" : [],
     *         "error" : []
     *     }
     * </pre>
     *
     * @return 응답 객체
     */

    public ResponseEntity<?> success() {
        return success(Collections.emptyList(), null, HttpStatus.OK);
    }


    public ResponseEntity<?> fail(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
                .state(status.value())
                .data(data)
                .result("fail")
                .massage(msg)
                .error(Collections.emptyList())
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * <p> 메세지를 가진 실패 응답을 반환한다. </p>
     * <pre>
     *     {
     *         "state" : HttpStatus Code,
     *         "result" : fail,
     *         "message" : message,
     *         "data" : [],
     *         "error" : [{error1}, {error2}...]
     *     }
     * </pre>
     *
     * @param msg 응답 바디 message 필드에 포함될 정보
     * @param status 응답 바디 status 필드에 포함될 응답 상태 코드
     * @return 응답 객체
     */
    public ResponseEntity<?> fail(String msg, HttpStatus status) {
        return fail(Collections.emptyList(), msg, status);
    }

    public ResponseEntity<?> invalidFields(LinkedList<LinkedHashMap<String, String>> errors) {
        Body body = Body.builder()
                .state(HttpStatus.BAD_REQUEST.value())
                .data(Collections.emptyList())
                .result("fail")
                .massage("")
                .error(errors)
                .build();
        return ResponseEntity.ok(body);
    }

    /**
     * <p> 필드 에러를 가진 실패 응답을 반환한다. </p>
     * <pre>
     *     {
     *          "state" : 400,
     *          "result" : fail,
     *          "message" : "",
     *          "data" : [],
     *          "error" : [{field1, message1}, {field2, message2}...]
     *     }
     * </pre>
     *
     * @param errors 응답 바디 error 필드에 포함될 정보
     *               - field : 에러가 발생한 필드명
     *               - message : 에러 메세지
     *               의 형태로 구성된 LinkedHashMap을 담은 LinkedList
     *               (Helper.refineErrors() 메소드로 생성 가능)
     *
     * @return 응답 객체
     *
     * 사용 예시 : return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
     * 사용 예시 : return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
     *
     * 사용 예시 : return response.invalidFields(Helper.refineErrors(errors));
     */
}
