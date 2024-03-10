package com.jjs.ClothingInventorySaleReformPlatform.controller.product;

import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductListDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.response.ProductListLikeDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.response.Response;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductLikeCountService;
import com.jjs.ClothingInventorySaleReformPlatform.service.product.ProductLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "상품 좋아요", description = "상품 상세페이지의 좋아요 API 입니다.")
public class ProductLikeController {

    private final ProductLikeService productLikeService;
    private final ProductLikeCountService productLikeCountService;
    private final Response response;

    @PostMapping("/product/all/detail/{productId}/like")
    @Operation(summary = "상품 좋아요 추가", description = "구매자가 상품 상세 페이지에서 비활성화 상태의 좋아요 버튼을 누르면 활성화된다. 비활성화 상태의 좋아요 버튼을 누르면 해당 요청을 보내며 활성화된다. 좋아요 정보는 db에 추가된다.")
    public ResponseEntity<?> addLike(@PathVariable Long productId) {
        try {
            productLikeService.addLike(productId);  // 좋아요 활성화 -> productLike 테이블에 좋아요한 상품 id와 로그인한 사용자 이메일 추가
            productLikeCountService.incrementLikeCount(productId);  // 좋아요를 활성화하면 productLikeCount 테이블의 좋아요 개수 집계 -> 증가

            return response.success(productId, "좋아요 추가 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return response.fail(productId, "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 좋아요 삭제
    @DeleteMapping("/product/all/detail/{productId}/like")
    @Operation(summary = "상품 좋아요 삭제", description = "구매자가 상품 상세 페이지에서 활성화 상태의 좋아요 버튼을 누르면 비활성화된다. 활성화 상태의 좋아요 버튼을 누르면 해당 요청을 보내며 비활성화된다. 기존의 좋아요 정보는 db에서 삭제된다.")
    public ResponseEntity<?> removeLike(@PathVariable Long productId) {
        try {
            productLikeService.removeLike(productId);  // 좋아요 비활성화 -> productLike 테이블의 해당 데이터 삭제
            productLikeCountService.decrementLikeCount(productId); // 좋아요를 비활성화하면 productLikeCount 테이블의 좋아요 개수 집계 -> 감소
            return response.success(productId, "좋아요 삭제 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return response.fail(productId, "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/product/all/detail/{productId}/like-count")
    @Operation(summary = "상품 좋아요 조회", description = "특정 상품의 좋아요 개수를 조회한다.")
    public ResponseEntity<?> getProductLikeCount(@PathVariable Long productId) {
        Long likeCount = productLikeCountService.getLikeCount(productId);
        return response.success(likeCount, "조회 성공", HttpStatus.OK);
    }

    @GetMapping("/product/all/detail/{productId}/like-status")
    @Operation(summary = "상품 좋아요 상태 조회", description = "로그인한 사용자가 특정 상품에 대해 좋아요를 눌렀는지 여부를 반환한다.")
    public ResponseEntity<?> checkLikeStatus(@PathVariable Long productId) {
        try {
            boolean isLiked = productLikeService.isLikedByPurchaser(productId);
            return response.success(isLiked, "좋아요 상태 조회 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return response.fail("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/product/all/like/desc")
    @Operation(summary = "상품 좋아요순 내림차순 조회", description = "상품들에 대하여 좋아요 개수 순서대로 내림차순으로 상품들을 목록으로 조회한다.")
    public ResponseEntity<?> getProductsOrderByLikes() {
        List<ProductListLikeDTO> products = productLikeCountService.getProductsOrderByLikeCountDesc();

        return response.success(products, "상품 좋아요 내림차순 정렬", HttpStatus.OK);
    }


}
