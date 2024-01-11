package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class LikeId implements Serializable {
    private static final long serialVersionUID = -5426907386441005679L;
    @Size(max = 30)
    @NotNull
    @Column(name = "PURCHASER_EMAIL", nullable = false, length = 30)
    private String purchaserEmail;  // 구매자 이메일

    @NotNull
    @Column(name = "PORTFOLIO_NUMBER", nullable = false)
    private Integer portfolioNumber;  // 포트폴리오 이메일

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LikeId entity = (LikeId) o;
        return Objects.equals(this.portfolioNumber, entity.portfolioNumber) &&
                Objects.equals(this.purchaserEmail, entity.purchaserEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolioNumber, purchaserEmail);
    }

}