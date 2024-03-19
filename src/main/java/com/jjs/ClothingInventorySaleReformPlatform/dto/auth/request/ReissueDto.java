package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReissueDto {

    @NotEmpty(message = "accessToken 을 입력해주세요.")
    private String accessToken;

    @NotEmpty(message = "refreshToken 을 입력해주세요.")
    private String refreshToken;
}
