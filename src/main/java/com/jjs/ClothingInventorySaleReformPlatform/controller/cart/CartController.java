package com.jjs.ClothingInventorySaleReformPlatform.controller.cart;

import com.jjs.ClothingInventorySaleReformPlatform.domain.PurchaserInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.Cart;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.Product;
import com.jjs.ClothingInventorySaleReformPlatform.dto.cart.CountDto;
import com.jjs.ClothingInventorySaleReformPlatform.dto.cart.ProductInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.repository.auth.PurchaserRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.cart.CartRepository;
import com.jjs.ClothingInventorySaleReformPlatform.repository.product.ProductRepository;
import com.jjs.ClothingInventorySaleReformPlatform.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "장바구니", description = "장바구니에 상품을 추가/조회/삭제하는 API 입니다. 장바구니는 구매자만 사용 가능하며, 구매자는 각자 한 개의 장바구니를 가질 수 있습니다. 장바구니는 구매자로 회원가입 시 자동으로 생성됩니다.")
public class CartController {

    private final CartService cartService;
    private final PurchaserRepository purchaserRepository;
    private final ProductRepository productRepository;
    private final Response response;
    private final CartRepository cartRepository;


    @PostMapping("/cart/purchaser/add/{productId}")
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
    @GetMapping("/cart/purchaser/{cartId}")
    @Operation(summary = "장바구니 상품 조회", description = "로그인한 회원(구매자)에 대한 장바구니 내의 상품을 조회합니다. ")
    public ResponseEntity<?> userCartView(@PathVariable Long cartId) {

        String currentUsername = getCurrentUsername();
        PurchaserInfo purchaserInfo = purchaserRepository.findPurchaserByEmail(currentUsername);

        if (purchaserInfo == null) {
            return response.fail("구매자 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);  // 구매자 정보를 찾을 수 없을 때 처리
        }

        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.isEmpty()) {
            return response.fail("장바구니를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);  // 없는 장바구니에 접근했을 때 처리
        }

        // 로그인한 사용자의 장바구니를 조회합니다.
        Cart userCart = cartRepository.findByPurchaserInfoEmail(currentUsername);
        if (userCart == null || !userCart.getId().equals(cartId)) {
            return response.fail("잘못된 장바구니에 접근했습니다.", HttpStatus.FORBIDDEN);  // 다른 사용자의 장바구니에 접근했을 때 처리
        }

        //Cart userCart = cart.get();
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
