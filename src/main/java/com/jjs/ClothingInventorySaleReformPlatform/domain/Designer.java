package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "DESIGNER", schema = "jjs")
public class Designer {  // 디자이너
    @Id
    @Size(max = 30)
    @Column(name = "EMAIL", nullable = false, length = 30)
    private String email;  // 이메일

    @Size(max = 15)
    @Column(name = "NICKNAME", length = 15)
    private String nickname;  // 닉네임

    @Size(max = 30)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 30)
    private String password;  // 비밀번호

    @Size(max = 15)
    @NotNull
    @Column(name = "PHONENUMBER", nullable = false, length = 15)
    private String phonenumber;  // 전화번호

    @Size(max = 40)
    @NotNull
    @Column(name = "ADDRESS", nullable = false, length = 40)
    private String address;  // 주소


    /*
    @OneToMany(mappedBy = "userEmail2")
    private Set<Completelist> completelists = new LinkedHashSet<>();
*/
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

}