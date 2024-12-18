package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "\"category\"")
public class Category extends BaseEntity{
    @Id
    String name;
    String description;
    @ManyToMany
    Set<Product> products = new HashSet<>();
}
