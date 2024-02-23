package com.jjs.ClothingInventorySaleReformPlatform.domain.reformrequest;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "REFORM_REQUEST_IMAGE")
public class ReformRequestImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_IMG_NUMBER", nullable = false)
    private Long id;  // 리폼 의뢰 이미지 번호

    @Column(name = "REQUEST_IMG", nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUEST_NUMBER")
    private ReformRequest reformRequest;

}
