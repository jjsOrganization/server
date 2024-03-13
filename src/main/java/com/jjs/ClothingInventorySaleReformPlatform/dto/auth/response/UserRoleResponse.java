package com.jjs.ClothingInventorySaleReformPlatform.dto.auth.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.SellerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleResponse {
    private String role;

    public UserRoleResponse(String role) {
        this.role = role;
    }

    public static UserRoleResponse from(User user) {
        return new UserRoleResponse(
                user.getRole()
        );
    }
}
