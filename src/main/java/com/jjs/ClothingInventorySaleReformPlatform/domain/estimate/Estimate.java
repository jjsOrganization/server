package com.jjs.ClothingInventorySaleReformPlatform.domain.estimate;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest.ReformRequest;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.PurchaserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ESTIMATE")
public class Estimate {  // 견적서

    // 견적서번호, 의뢰번호, 의뢰자이메일, 디자이너이메일, 견적서정보, 견적서사진, 가격

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTIMATE_NUMBER", nullable = false)
    private Integer id;  // 견적서 번호

    @NotNull
    @Lob
    @Column(name = "ESTIMATE_INFO", nullable = false)
    private String estimateInfo;  // 견적서 정보

    @NotNull
    @Column(name = "ESTIMATE_IMG", nullable = false)
    private byte[] estimateImg;  // 견적서 사진

    @NotNull
    @Column(name = "PRICE", nullable = false)
    private Integer price;  // 가격

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

    /*

    // 김영한 jpa 방식으로 수정 - 4
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAIL")
    private Seller clientEmail2;


    @OneToMany(mappedBy = "estimateNumber")
    private Set<Completelist> completelists = new LinkedHashSet<>();

    @OneToMany(mappedBy = "estimateNumber")
    private Set<Progressmanagement> progressmanagements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "estimateNumber")
    private Set<Question> questions = new LinkedHashSet<>();
*/
}