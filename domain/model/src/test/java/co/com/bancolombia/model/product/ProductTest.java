package co.com.bancolombia.model.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldBuildProductWithCorrectValues() {
        Integer id = 101;
        String name = "Product A";
        Integer stock = 50;
        Integer branchId = 5;

        Product product = Product.builder()
                .id(id)
                .name(name)
                .stock(stock)
                .branchId(branchId)
                .build();

        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(stock, product.getStock());
        assertEquals(branchId, product.getBranchId());
    }

    @Test
    void shouldCreateModifiedCopyWithToBuilder() {
        Product originalProduct = Product.builder()
                .id(101)
                .name("Product A")
                .stock(50)
                .branchId(5)
                .build();

        Product modifiedProduct = originalProduct.toBuilder()
                .stock(100)
                .name("Modified Product A")
                .build();

        assertEquals(101, modifiedProduct.getId());
        assertEquals("Modified Product A", modifiedProduct.getName());
        assertEquals(100, modifiedProduct.getStock());
        assertEquals(5, modifiedProduct.getBranchId());
        assertNotEquals(originalProduct, modifiedProduct);
    }

    @Test
    void shouldMaintainEqualityForSameProperties() {
        Product product1 = Product.builder()
                .id(101)
                .name("Product A")
                .stock(50)
                .branchId(5)
                .build();

        Product product2 = Product.builder()
                .id(101)
                .name("Product A")
                .stock(50)
                .branchId(5)
                .build();

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }
}