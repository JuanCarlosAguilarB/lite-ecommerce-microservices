package com.inventory.inventory.infrastructure.persistence;

import com.inventory.inventory.domain.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntity {
    @Id
    private UUID id;

    @Column("product_id")
    private UUID productId;

    private Integer quantity;

    @Column("creation_date")
    private LocalDateTime creationDate;

    @Column("update_date")
    private LocalDateTime updateDate;

    public static Inventory toDomain(InventoryEntity entity){
        return new Inventory(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getCreationDate(),
                entity.getUpdateDate());
    }

}

