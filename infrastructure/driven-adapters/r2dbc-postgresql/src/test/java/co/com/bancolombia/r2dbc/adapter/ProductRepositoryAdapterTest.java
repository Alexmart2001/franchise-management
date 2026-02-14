package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class ProductRepositoryAdapterTest {

    @Mock
    private ProductDataRepository productDataRepository;

    @InjectMocks
    private ProductRepositoryAdapter productRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindProductByIdSuccessfully() {
        Integer productId = 1;
        ProductEntity productEntity = new ProductEntity(productId, "Product A", 10, 1);
        when(productDataRepository.findById(productId)).thenReturn(Mono.just(productEntity));

        Mono<Product> result = productRepositoryAdapter.findById(productId);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.getId().equals(productId))
                .verifyComplete();

        verify(productDataRepository).findById(productId);
    }

    @Test
    void shouldNotFindProductById() {
        Integer productId = 99;
        when(productDataRepository.findById(productId)).thenReturn(Mono.empty());

        Mono<Product> result = productRepositoryAdapter.findById(productId);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(productDataRepository).findById(productId);
    }

    @Test
    void shouldFindMaxStockByFranchise() {
        Integer franchiseId = 1;
        ProductEntity productEntity1 = new ProductEntity(1, "Product A", 100, franchiseId);
        ProductEntity productEntity2 = new ProductEntity(2, "Product B", 150, franchiseId);

        when(productDataRepository.findMaxStockByFranchise(franchiseId))
                .thenReturn(Flux.just(productEntity1, productEntity2));

        Flux<Product> result = productRepositoryAdapter.findMaxStockByFranchise(franchiseId);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.getId().equals(1))
                .expectNextMatches(product -> product.getId().equals(2))
                .verifyComplete();

        verify(productDataRepository).findMaxStockByFranchise(franchiseId);
    }

    @Test
    void shouldDeleteProductByIdAndBranch() {
        Integer productId = 1;
        Integer branchId = 2;
        ProductEntity entity = new ProductEntity(productId, "Product A", 100, branchId);

        when(productDataRepository.findById(productId)).thenReturn(Mono.just(entity));
        when(productDataRepository.deleteById(productId)).thenReturn(Mono.empty());

        Mono<Void> result = productRepositoryAdapter.deleteByIdAndBranch(productId, branchId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(productDataRepository).deleteById(productId);
    }

    @Test
    void shouldNotDeleteProductIfNotBelongToBranch() {
        Integer productId = 1;
        Integer branchId = 99;
        ProductEntity entity = new ProductEntity(productId, "Product A", 100, 2);

        when(productDataRepository.findById(productId)).thenReturn(Mono.just(entity));

        Mono<Void> result = productRepositoryAdapter.deleteByIdAndBranch(productId, branchId);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(productDataRepository, never()).deleteById(productId);
    }

    @Test
    void shouldVerifyProductExistsInBranch() {
        Integer productId = 1;
        Integer branchId = 2;
        ProductEntity entity = new ProductEntity(productId, "Product A", 100, branchId);

        when(productDataRepository.findById(productId)).thenReturn(Mono.just(entity));

        Mono<Boolean> result = productRepositoryAdapter.existsInBranch(productId, branchId);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(productDataRepository).findById(productId);
    }

    @Test
    void shouldNotVerifyProductExistsInBranch() {
        Integer productId = 1;
        Integer branchId = 99;
        ProductEntity entity = new ProductEntity(productId, "Product A", 100, 2);

        when(productDataRepository.findById(productId)).thenReturn(Mono.just(entity));

        Mono<Boolean> result = productRepositoryAdapter.existsInBranch(productId, branchId);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();

        verify(productDataRepository).findById(productId);
    }

    @Test
    void shouldFindProductByIdAndBranch() {
        Integer productId = 1;
        Integer branchId = 2;
        ProductEntity entity = new ProductEntity(productId, "Product A", 100, branchId);

        when(productDataRepository.findByIdAndBranch(productId, branchId))
                .thenReturn(Mono.just(entity));

        Mono<Product> result = productRepositoryAdapter.findByIdAndBranch(productId, branchId);

        StepVerifier.create(result)
                .expectNextMatches(product -> product.getId().equals(productId))
                .verifyComplete();

        verify(productDataRepository).findByIdAndBranch(productId, branchId);
    }
}