package com.jjs.ClothingInventorySaleReformPlatform.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Setter
@Getter
public abstract class BaseEntity extends BaseTimeEntity {  // 작성자, 수정자, 작성시간, 수정시간을 상속만으로 엔티티에 추가해줌

    @CreatedBy  // 글 작성자 -> sellerInfo와 조인할 필요 없음
    @Column(updatable = false)
    private String createBy;

    @LastModifiedBy
    private String modifiedBy;
}
