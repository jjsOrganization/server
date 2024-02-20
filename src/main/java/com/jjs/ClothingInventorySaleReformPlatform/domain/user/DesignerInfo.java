package com.jjs.ClothingInventorySaleReformPlatform.domain.user;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DESIGNER")
public class DesignerInfo {  // 디자이너 부가 정보

    @Id
    private String email;  // User 엔티티의 email과 동일

    @Column(name = "ADDRESS", nullable = false)
    private String address;  // 상점 이름

    @OneToOne
    @MapsId  // PurchaseInfo 엔티티의 PK를 User 엔티티의 PK와 동일하게 매핑
    @JoinColumn(name = "EMAIL")  // User 테이블의 email을 외래 키로 사용
    private User user;  // User 엔티티에 대한 참조



//    public static Designer toDesigner(DesignerDTO designerDTO) {
//        Designer designer = new Designer();
//
//        designer.setEmail(designerDTO.getEmail());
//        designer.setPassword(designerDTO.getPassword());
//        designer.setDesignerName(designerDTO.getDesignerName());
//        designer.setName(designerDTO.getName());
//        designer.setBusinessNumber(designerDTO.getBusinessNumber());
//        designer.setAddress(designerDTO.getAddress());
//        designer.setPhoneNumber(designerDTO.getPhoneNumber());
//
//        return designer;
//    }

    /*
    @OneToMany(mappedBy = "userEmail2")
    private Set<Completelist> completelists = new LinkedHashSet<>();
    */

/*
    @OneToMany(mappedBy = "designerEmail")
    private Set<Estimate> estimates = new LinkedHashSet<>();

    @OneToMany(mappedBy = "desingerEmail")
    private Set<Portfolio> portfolios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "designerEmail")
    private Set<Progressmanagement> progressmanagements = new LinkedHashSet<>();

    @OneToMany(mappedBy = "designerEmail")
    private Set<Question> questions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "designerEmail")
    private Set<Request> requests = new LinkedHashSet<>();

    // 김영한 jpa 방식으로 수정 - 1
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_EMAIL")
    private Revenuetally revenuetally;
*/
}