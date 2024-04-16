package com.jjs.ClothingInventorySaleReformPlatform.domain.user.dto.updateRequest.patchUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDTO {
    private String password;
    private String rePassword;
}
