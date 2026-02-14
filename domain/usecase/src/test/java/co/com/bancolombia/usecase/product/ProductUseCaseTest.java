package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.commons.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductUseCase productUseCase;

    @Mock
    private BranchRepository branchRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateName_ValidData_ShouldUpdateName() {
        Product existingProduct = Product.builder()
                .id(1)
                .name("Old Name")
                .stock(50)
                .branchId(1)
                .build();

        when(productRepository.findByIdAndBranch(1, 1)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(productUseCase.updateName(1, 1, "New Name"))
                .expectNextMatches(product -> "New Name".equals(product.getName()))
                .verifyComplete();
    }

    @Test
    void testUpdateName_InvalidName_ShouldThrowError() {
        when(productRepository.findByIdAndBranch(anyInt(), anyInt())).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.updateName(1, 1, ""))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_NAME".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void testUpdateStock_ValidData_ShouldUpdateStock() {
        Product existingProduct = Product.builder()
                .id(1)
                .name("Valid Product")
                .stock(50)
                .branchId(1)
                .build();

        when(productRepository.findByIdAndBranch(1, 1)).thenReturn(Mono.just(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(productUseCase.updateStock(1, 1, 80))
                .expectNextMatches(product -> product.getStock().equals(80))
                .verifyComplete();
    }

    @Test
    void testUpdateStock_InvalidStock_ShouldThrowError() {
        when(productRepository.findByIdAndBranch(anyInt(), anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.updateStock(1, 1, -10))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_STOCK".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void testFindMaxStockByFranchise_ValidId_ShouldReturnProducts() {
        Product product1 = Product.builder()
                .id(1)
                .name("Product 1")
                .stock(100)
                .branchId(1)
                .build();

        Product product2 = Product.builder()
                .id(2)
                .name("Product 2")
                .stock(200)
                .branchId(1)
                .build();

        when(productRepository.findMaxStockByFranchise(1)).thenReturn(Flux.just(product1, product2));

        StepVerifier.create(productUseCase.findMaxStockByFranchise(1))
                .expectNext(product1, product2)
                .verifyComplete();
    }

    @Test
    void testFindById_ValidId_ShouldReturnProduct() {
        Product product = Product.builder()
                .id(1)
                .name("Valid Product")
                .stock(50)
                .branchId(1)
                .build();

        when(productRepository.findById(1)).thenReturn(Mono.just(product));

        StepVerifier.create(productUseCase.findById(1))
                .expectNext(product)
                .verifyComplete();
    }

    @Test
    void testFindById_ProductNotFound_ShouldThrowError() {
        when(productRepository.findById(1)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.findById(1))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "PRODUCT_NOT_FOUND".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    private Mono<Void> invokeValidateProduct(Product product) throws Exception {
        Method validateProductMethod = ProductUseCase.class.getDeclaredMethod("validateProduct", Product.class);
        validateProductMethod.setAccessible(true);
        return (Mono<Void>) validateProductMethod.invoke(productUseCase, product);
    }

    @Test
    void validateProduct_NullProduct_ShouldThrowError() throws Exception {
        Mono<Void> result = invokeValidateProduct(null);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "PRODUCT_NULL".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateProduct_InvalidName_ShouldThrowError() throws Exception {
        Product product = Product.builder()
                .id(1)
                .name("")
                .stock(10)
                .branchId(1)
                .build();

        Mono<Void> result = invokeValidateProduct(product);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_NAME".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateProduct_InvalidStock_ShouldThrowError() throws Exception {
        Product product = Product.builder()
                .id(1)
                .name("Valid Name")
                .stock(-10)
                .branchId(1)
                .build();

        Mono<Void> result = invokeValidateProduct(product);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_STOCK".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateProduct_InvalidBranchId_ShouldThrowError() throws Exception {
        Product product = Product.builder()
                .id(1)
                .name("Valid Name")
                .stock(10)
                .branchId(-1)
                .build();

        Mono<Void> result = invokeValidateProduct(product);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_BRANCH".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateProduct_ValidProduct_ShouldComplete() throws Exception {
        Product product = Product.builder()
                .id(1)
                .name("Valid Name")
                .stock(10)
                .branchId(1)
                .build();

        Mono<Void> result = invokeValidateProduct(product);

        StepVerifier.create(result)
                .verifyComplete();
    }
}