package com.jjs.ClothingInventorySaleReformPlatform.domain.chat;

import com.jjs.ClothingInventorySaleReformPlatform.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ChatRoom")
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String purchaserEmail;

    private String designerEmail;


}
