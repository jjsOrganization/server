package com.jjs.ClothingInventorySaleReformPlatform.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`LIKE`", schema = "jjs")
public class Like {  // 좋아요 (테이블)
    @EmbeddedId
    private LikeId id;  //

    //TODO [JPA Buddy] generate columns from DB
}