package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "\"inventory\"")
public class Inventory extends BaseEntity {
    @Id
    String id;
    int quantity;
    // owning side since it references product (has the foreign key)
    @OneToOne
    @MapsId
    private Product product;
}
