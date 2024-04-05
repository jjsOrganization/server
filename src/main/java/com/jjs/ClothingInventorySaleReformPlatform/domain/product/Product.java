package com.jjs.ClothingInventorySaleReformPlatform.domain.product;

import com.jjs.ClothingInventorySaleReformPlatform.common.entity.BaseEntity;
import com.jjs.ClothingInventorySaleReformPlatform.domain.cart.CartProduct;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.like.ProductLike;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.like.ProductLikeCount;
import com.jjs.ClothingInventorySaleReformPlatform.dto.product.ProductFormDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "PRODUCT")  // goods
@NoArgsConstructor
public class Product extends BaseEntity {  // BaseEntity가 등록시간, 수정시간, 만든 사람, 수정자 정보 포함해줌
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본키 자동 생성 - IDENTITY : 기본키 생성을 db에 위임(원래 회원도 이걸로 권장)
    @Column(name = "product_id")
    private Long id;  // 상품 코드 (PK)

    @Column(name = "product_name", nullable = false, length = 30)
    private String productName;  // 상품명

    @Column(nullable = false)
    private int price;  // 가격

    @Column(nullable = false)
    private int productStock;  // 재고수

    @Column(length = 1000)
    private String productDetailText;  // 상품 설명(보류)

    @Enumerated(EnumType.STRING)
    private ProductSellStatus productSellStatus; // 상품 상태 (품절 or 판매 가능)[SELL, SOLD_OUT]

    // 상품 삭제 시 이미지 DB 도 같이 삭제 , cascade 옵션
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<ProductImg> productImg;

    /**
     * 상품 삭제 시 장바구니에 담긴 상품들도 삭제 - 근데 이렇게 하는 것보다 상품의 상태를 "판매중", "삭제됨" 이런식으로 설정하는게 더 낫다고 함
     * mappedBy = "product" : CartProduct 엔티티 내에 Product 엔티티를 참조하는 필드 이름
     * cascade = CascadeType.ALL : Product 엔티티에 대한 모든 변경(생성, 수정, 삭제 등)이 관련된 CartProduct 엔티티에도 적용
     **/
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ProductLike> likes;

    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private ProductLikeCount likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(Long id) {
        this.id = id;
    }

    public void updateProduct(ProductFormDTO productFormDTO) {
        this.productName = productFormDTO.getProductName();
        this.price = productFormDTO.getPrice();
        this.productStock = productFormDTO.getProductStock();
        this.productDetailText = productFormDTO.getItemDetail();
        this.productSellStatus = productFormDTO.getProductSellStatus();
    }
}