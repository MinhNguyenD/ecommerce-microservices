package com.ecommerce.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "\"role\"")
public class Role {
    @Id
    String name;
    String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    Set<Permission> permissions;
}
