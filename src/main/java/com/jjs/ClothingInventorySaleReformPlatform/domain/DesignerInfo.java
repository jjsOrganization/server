package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DESIGNERINFO")
public class DesignerInfo {  // 디자이너 부가 정보
    @Id
    @Size(max = 30)
    @Column(name = "EMAIL", nullable = false, length = 30, unique = true)
    private String email;  // 이메일

    @Size(max = 30)
    @Column(name = "ADDRESS", nullable = false, length = 30)
    private String address;

    @Size(max = 10)
    @Column(name = "BUSINESSNUMBER", length = 10)
    private String businessNumber;  // 사업자 번호 (보류?)



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