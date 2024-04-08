package com.jjs.ClothingInventorySaleReformPlatform.dto.reform.estimate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

/**
 * 수락 or 거절 요청 JSON값으로 받기 위한 클래스 (@RequestBody 때문에 json -> 자바 객체로 변환하기 위해 생성함)
 */
public class ClientResponse {
    private String clientResponse;

}
