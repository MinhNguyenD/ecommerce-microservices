package com.ecommerce.product_service.entity;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity {
    @CreatedDate
    Instant createdDate; // Automatically populated by the persistence framework

    @LastModifiedDate
    Instant lastModifiedDate; // Automatically updated by the persistence framework

    @CreatedBy
    String createdBy; // Automatically populated by the persistence framework

    @LastModifiedBy
    String modifiedBy; // Automatically updated by the persistence framework
}
