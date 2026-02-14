package co.com.bancolombia.r2dbc.entity;

import co.com.bancolombia.model.product.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityTest {

    @Test
    void shouldConvertFromDomainToEntity() {
        Product product = Product.builder()
                .id(101)
                .name("Test Product")
                .stock(50)
                .branchId(1)
                .build();

        ProductEntity productEntity = ProductEntity.fromDomain(product);

        assertThat(productEntity).isNotNull();
        assertThat(productEntity.getId()).isEqualTo(product.getId());
        assertThat(productEntity.getName()).isEqualTo(product.getName());
        assertThat(productEntity.getStock()).isEqualTo(product.getStock());
        assertThat(productEntity.getBranchId()).isEqualTo(product.getBranchId());
    }

    @Test
    void shouldConvertEntityToDomain() {
        ProductEntity productEntity = ProductEntity.builder()
                .id(102)
                .name("Another Product")
                .stock(150)
                .branchId(10)
                .build();

        Product product = productEntity.toDomain();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(productEntity.getId());
        assertThat(product.getName()).isEqualTo(productEntity.getName());
        assertThat(product.getStock()).isEqualTo(productEntity.getStock());
        assertThat(product.getBranchId()).isEqualTo(productEntity.getBranchId());
    }

    @Test
    void shouldBuildEntityProperly() {
        ProductEntity productEntity = ProductEntity.builder()
                .id(200)
                .name("Example Product")
                .stock(300)
                .branchId(2)
                .build();

        assertThat(productEntity).isNotNull();
        assertThat(productEntity.getId()).isEqualTo(200);
        assertThat(productEntity.getName()).isEqualTo("Example Product");
        assertThat(productEntity.getStock()).isEqualTo(300);
        assertThat(productEntity.getBranchId()).isEqualTo(2);
    }

    @Test
    void shouldBuildDomainProperly() {
        Product product = Product.builder()
                .id(201)
                .name("Domain Product")
                .stock(400)
                .branchId(5)
                .build();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(201);
        assertThat(product.getName()).isEqualTo("Domain Product");
        assertThat(product.getStock()).isEqualTo(400);
        assertThat(product.getBranchId()).isEqualTo(5);
    }
}