package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.dto.PortfolioInfoDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.User;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PORTFOLIO")
public class Portfolio extends BaseEntity {  // 포트폴리오 - 디자이너이메일, 설명, 이전결과사진, 이전결과설명, 포트폴리오번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PORTFOLIO_ID", nullable = false)
    private Long id;  // 포트폴리오 번호

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESINGER_EMAIL", nullable = false)
    private User designerEmail;  // 디자이너 이메일

    @Column(name = "DESIGNER_NAME")
    private String name;

    @NotNull
    @Column(name = "EXPLANATION", nullable = false, length = 1000)  // @Lob는 매우 큰 데이터(Mb 단위)를 저장할 때 사용한다고 하여 varchar(1000)으로 수정함
    private String explanation;  // 설명

    @NotNull
    @Column(name = "DESIGNER_IMAGE", nullable = false, length = 1000)
    private String designerImage;  // 디자이너 사진

    @Column(name = "PRICE", length = 1000)
    private String reformPrice;  // 가격표 이미지

    /**
     * 포트폴리오 정보 DTO 객체로 변환 해주는 메소드
     * @param portfolio
     * @return
     */

    public static PortfolioInfoDTO convertToDTO(Portfolio portfolio) {
        PortfolioInfoDTO portfolioInfoDTO = new PortfolioInfoDTO();
        portfolioInfoDTO.setPortfolioId(portfolio.getId());
        portfolioInfoDTO.setExplanation(portfolio.getExplanation());
        portfolioInfoDTO.setDesignerName(portfolio.getName());
        portfolioInfoDTO.setDesignerImagePath(portfolio.getDesignerImage());
        portfolioInfoDTO.setPriceImagePath(portfolio.getReformPrice());

        User designer = portfolio.getDesignerEmail();
        portfolioInfoDTO.setDesignerEmail(designer.getEmail());

        return portfolioInfoDTO;
    }

}