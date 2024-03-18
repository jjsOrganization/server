package com.jjs.ClothingInventorySaleReformPlatform.controller.auth;

import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest.patchPurchaser.UpdateAddressDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest.patchUser.UpdatePasswordDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.auth.updateRequest.patchUser.UpdatePhoneNumberDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.jwt.provider.JwtTokenProvider;
import com.jjs.ClothingInventorySaleReformPlatform.service.auth.UserService;
import com.jjs.ClothingInventorySaleReformPlatform.service.auth.UserUpdateService;
import com.jjs.ClothingInventorySaleReformPlatform.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequiredArgsConstructor
@Tag(name = "회원 정보 수정 컨트롤러", description = "로그인, 로그인 테스트, 회원가입 API")
public class UserUpdateController {

    private final UserService userService;
    private final UserUpdateService userUpdateService;
    private final CartService cartService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Response response;

    /**
     * 구매자, 판매자, 디자이너
     */
    @PatchMapping("/auth/update/phoneNumber")
    @Operation(summary = "전화번호 변경", description = "회원 정보 수정 시, 회원의 전화번호를 변경한다. 구매자, 판매자, 디자이너 공용으로 사용")
    public ResponseEntity<?> updatePhoneNumber(@RequestBody UpdatePhoneNumberDTO updateDTO) {
        userUpdateService.updatePhoneNumber(updateDTO);
        return response.success("전화번호 수정 완료", HttpStatus.OK);
    }

    /**
     * 구매자, 판매자, 디자이너
     */
    @PatchMapping("/auth/update/password")
    @Operation(summary = "비밀번호 변경", description = "회원 정보 수정 시, 회원의 비밀번호를 변경한다. 구매자, 판매자, 디자이너 공용으로 사용")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO updateDTO) {
        userUpdateService.updatePassword(updateDTO);
        return response.success("비밀번호 변경 완료", HttpStatus.OK);
    }

    /**
     * 구매자
     */
    @PatchMapping("/auth/update-purchaser/address")
    @Operation(summary = "주소 변경", description = "회원 정보 수정 시, 회원의 주소를 변경한다. 구매자만 해당")
    public ResponseEntity<?> updatePurchaserAddress(@RequestBody UpdateAddressDTO updateDTO) {
        userUpdateService.updatePurchaserAddress(updateDTO);
        return response.success("주소 수정 완료", HttpStatus.OK);
    }

    /**
     * 디자이너
     */
    @PatchMapping("/auth/update-designer/address")
    @Operation(summary = "주소 변경", description = "회원 정보 수정 시, 회원의 주소를 변경한다. 디자이너만 해당")
    public ResponseEntity<?> updateDesignerAddress(@RequestBody UpdateAddressDTO updateDTO) {
        userUpdateService.updateAddress(updateDTO);
        return response.success("주소 수정 완료", HttpStatus.OK);
    }

}
