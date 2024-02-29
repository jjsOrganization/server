package com.jjs.ClothingInventorySaleReformPlatform.domain;

import com.jjs.ClothingInventorySaleReformPlatform.common.entity.BaseEntity;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
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
    @Lob
    @Column(name = "EXPLANATION", nullable = false)
    private String explanation;  // 설명

    @NotNull
    @Column(name = "DESIGNER_IMAGE", nullable = false)
    private String designerImage;  // 디자이너 사진


}