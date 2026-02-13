package co.com.bancolombia.r2dbc.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import co.com.bancolombia.model.product.Product;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("product")
public class ProductEntity {
    @Id
    private Integer id;
    private String name;
    private Integer stock;
    private Integer branchId;

    public static ProductEntity fromDomain(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }

    public Product toDomain() {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .stock(this.stock)
                .branchId(this.branchId)
                .build();
    }
}