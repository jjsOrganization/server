package com.jjs.ClothingInventorySaleReformPlatform.domain.reform.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ESTIMATE")
public class Estimate {  // 견적서

    // 견적서번호, 견적서정보, 사진, 가격, 의뢰자이메일, 디자이너이메일, 의뢰번호, 견적서 상태,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTIMATE_NUMBER", nullable = false)
    private Long id;  // 견적서 번호

    @Column(name = "ESTIMATE_INFO", nullable = false, length = 2000)
    private String estimateInfo;  // 견적서 정보(상세글)

    @OneToMany(mappedBy = "estimate", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY) // 리폼 의뢰 첨부 이미지 연결
    private List<EstimateImage> estimateImg = new ArrayList<>();

    @Column(name = "PRICE", nullable = false)
    private String price;  // 가격

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENT_EMAIL", nullable = false)
    private PurchaserInfo clientEmail;  // 의뢰자 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DESIGNER_EMAIL", nullable = false)
    private DesignerInfo designerEmail;  // 디자이너 이메일

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUEST_NUMBER", nullable = false)
    private ReformRequest requestNumber;  // 의뢰 번호

    @Enumerated(EnumType.STRING)
    private EstimateStatus estimateStatus;  // 견적서 상태 REQUEST_WAITING, REQUEST_REJECTED, REQUEST_ACCEPTED

}