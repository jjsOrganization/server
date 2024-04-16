package com.jjs.ClothingInventorySaleReformPlatform.domain.cart.controller;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.entity.Cart;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.dto.CountDto;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.dto.ProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.returnResponse.Response;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.repository.CartRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.repository.ProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "장바구니", description = "장바구니에 상품을 추가/조회/삭제하는 API 입니다. 장바구니는 구매자만 사용 가능하며, 구매자는 각자 한 개의 장바구니를 가질 수 있습니다. 장바구니는 구매자로 회원가입 시 자동으로 생성됩니다.")
public class CartController {

    private final CartService cartService;
    private final PurchaserRepository purchaserRepository;
    private final ProductRepository productRepository;
    private final Response response;
    private final CartRepository cartRepository;


    @PostMapping(value = "/cart/purchaser/add/{productId}")
    @Operation(summary = "장바구니 상품 추가", description = "장바구니에 상품을 추가합니다. 상품 추가 시, 상품의 갯수를 함께 입력합니다.")
    public ResponseEntity<?> addCart(@PathVariable Long productId, @RequestBody CountDto countDto) {
        // 현재 인증된 사용자의 이메일(또는 사용자 이름)을 가져옵니다.
        String currentUsername = getCurrentUsername();

        // 구매자 정보 조회
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(currentUsername);
        if (purchaserInfo == null) {
            // 적절한 예외 처리 또는 오류 응답을 여기에 추가
            return response.fail("구매자 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 상품 정보 조회
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            // 적절한 예외 처리 또는 오류 응답을 여기에 추가
            return response.fail("상품 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            // 장바구니에 상품 추가
            cartService.addCart(purchaserInfo, product, countDto.getCount());
            String productName = product.getProductName();
            return response.success(productName, "장바구니에 상품이 추가되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return response.fail(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }



    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null; // 또는 예외 처리
    }

    // 특정 사용자의 장바구니 상품 조회
    @GetMapping("/cart/purchaser")
    @Operation(summary = "장바구니 상품 조회", description = "로그인한 회원(구매자)에 대한 장바구니 내의 상품을 조회합니다. ")
    public ResponseEntity<?> userCartView() {

        // 현재 로그인한 사용자의 이메일을 가져옴
        String currentUsername = getCurrentUsername();

        // 현재 로그인한 사용자의 구매자 정보를 조회
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(currentUsername);
        if (purchaserInfo == null) {
            return response.fail("구매자 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);  // 구매자 정보를 찾을 수 없을 때 처리
        }

        // 로그인한 사용자의 장바구니 조회
        Cart userCart = cartRepository.findByPurchaserInfoEmail(currentUsername);
        if (userCart == null) {
            return response.fail("장바구니를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);  // 없는 장바구니에 접근했을 때 처리
        }

        // 장바구니에 담긴 상품 목록 조회
        List<ProductInfoDTO> cartProducts = cartService.userCartView(userCart);
        return response.success(cartProducts, "장바구니 상품 조회에 성공했습니다.", HttpStatus.OK);  // 성공 응답
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/cart/purchaser/delete/{productId}")
    @Operation(summary = "장바구니 내 상품 삭제", description = "장바구니 내의 상품 또는 상품들을 삭제합니다.")
    public ResponseEntity<?> cartProductDelete(@PathVariable long productId) {
        try {
            cartService.cartProductDelete(productId);
            return response.success(productId, "장바구니 상품이 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalStateException ex) {
            // 장바구니를 찾을 수 없을 때의 처리
            return response.fail("현재 사용자에 대한 장바구니를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            // 장바구니 내에 상품이 없을 때의 처리
            return response.fail("장바구니 내에 해당 상품이 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

}
