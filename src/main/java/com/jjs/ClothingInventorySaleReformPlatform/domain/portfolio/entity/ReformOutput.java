package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity;

import com.jjs.ClothingInventorySaleReformPlatform.domain.reform.entity.progressManagement.Progressmanagement;
import com.jjs.ClothingInventorySaleReformPlatform.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@Table(name = "OUTPUT")
public class ReformOutput extends BaseEntity {
    /**
     * 형상관리 번호로 작성 가능해야 함 -> 디자이너는 리폼 완료 후 해당 리폼 구역의 버튼으로 작업물 등록이 가능하도록 해야 함.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OUTPUT_ID", nullable = false)
    private Long id;

    @Column(name = "TITLE")
    private String title;  // 제목

    @Column(name = "DESIGNER_NAME")
    private String name;  // 디자이너명

    @Column(name = "PRODUCT_IMG_URL", length = 1000)
    private String productImgUrl;  // 리폼 전 사진 - 상품 사진 -> 형상관리의 productImgUrl

    @Column(name = "REFORM_REQUEST_IMG_URL", length = 1000)
    private String reformRequestImgUrl;  // 구매자 요청 사진 -> 요청서에서 [0]사진만 가져옴

    @Column(name = "ESTIMATE_IMG_URL", length = 1000)
    private String estimateImgUrl;  // 디자이너 견적서 사진 -> 견적서에서 가져옴

    @Column(name = "COMPLETE_IMG_URL", length = 1000)
    private String completeImgUrl;  // 리폼 완료 사진 -> 형상관리에서 completeImgUrl

    @NotNull
    @Column(name = "WORKING_PERIOD")
    private String workingPeriod;  // 리폼 기간 -> 형상관리 엔티티 마지막 업데이트일 - 요청서 엔티티의 등록일

    @NotNull
    @Column(name = "CATEGORY")
    private String category;  // 리폼 부위

    @NotNull
    @Column(name = "PRICE")
    private int price;  // 리폼 가격

    @NotNull
    @Column(name = "EXPLANATION", nullable = false, length = 1000)  // @Lob는 매우 큰 데이터(Mb 단위)를 저장할 때 사용한다고 하여 varchar(1000)으로 수정함
    private String explanation;  // 작업물 내용 작성

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRESS_NUMBER")
    private Progressmanagement progress; // Progressmanagement 엔티티 참조


    /**
     * 리폼 기간을 계산하여 설정하는 메서드.
     * @param requestStartDate 리폼 요청 시작 날짜
     * @param completeDate 리폼 완료 날짜
     */
    public String calWorkingPeriod(LocalDate requestStartDate, LocalDate completeDate) {
        if (requestStartDate != null && completeDate != null) {
            long days = ChronoUnit.DAYS.between(completeDate, requestStartDate) + 1;
            return String.valueOf(days) + "일";
        } else {
            return null;
        }
    }


}

