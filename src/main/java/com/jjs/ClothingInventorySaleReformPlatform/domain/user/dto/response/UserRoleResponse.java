package com.jjs.ClothingInventorySaleReformPlatform.domain.user.dto.response;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.User;
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
