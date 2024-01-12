package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WATERSAVEDTALLY")
public class Watersavedtally {  // 절약된 물 집계

    @Id
    @Column(name = "ID")
    private Integer id;  // 쓸모없는 컬럼

    @NotNull
    @Column(name = "TOTAL_AMOUNT_WATER_SAVED", nullable = false)
    private Integer totalAmountWaterSaved;  // 절약된 물 총량

    //TODO [JPA Buddy] generate columns from DB
}