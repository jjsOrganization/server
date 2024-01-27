package com.jjs.ClothingInventorySaleReformPlatform.domain.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="color")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorEntity {
    @Id
    @Column(name="colorId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long colorId;

    @Column( name="colorName", nullable=false, length=100 )
    private String colorName;
}
