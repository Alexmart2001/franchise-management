package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.ProductRequest;
import co.com.bancolombia.api.dto.UpdateProductNameRequest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.product.ProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class ProductHandlerTest {

    @Mock
    private ProductUseCase productUseCase;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private ProductHandler productHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        ProductRequest request = new ProductRequest("Product A", 10, 1);
        Product product = Product.builder().id(1).name("Product A").stock(10).branchId(1).build();
        Branch branch = Branch.builder().id(1).name("Branch A").build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.bodyToMono(ProductRequest.class)).thenReturn(Mono.just(request));
        when(productUseCase.create(any(Product.class))).thenReturn(Mono.just(product));
        when(branchRepository.findById(1)).thenReturn(Mono.just(branch));

        Mono<ServerResponse> responseMono = productHandler.create(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(productUseCase).create(any(Product.class));
        verify(branchRepository).findById(1);
    }

    @Test
    void shouldFindProductByIdSuccessfully() {
        Integer productId = 1;
        Product product = Product.builder().id(productId).name("Product A").stock(10).branchId(1).build();
        Branch branch = Branch.builder().id(1).name("Branch A").build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(productId.toString());
        when(productUseCase.findById(productId)).thenReturn(Mono.just(product));
        when(branchRepository.findById(1)).thenReturn(Mono.just(branch));

        Mono<ServerResponse> responseMono = productHandler.findById(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(productUseCase).findById(productId);
        verify(branchRepository).findById(1);
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExistById() {
        Integer productId = 999;
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(productId.toString());
        when(productUseCase.findById(productId)).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = productHandler.findById(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(NOT_FOUND))
                .verifyComplete();

        verify(productUseCase).findById(productId);
        verify(branchRepository, never()).findById(anyInt());
    }

    @Test
    void shouldUpdateProductNameSuccessfully() {
        Integer productId = 1;
        Integer branchId = 2;
        UpdateProductNameRequest updateRequest = new UpdateProductNameRequest("Updated Product Name");
        Product updatedProduct = Product.builder().id(productId).name("Updated Product Name").branchId(branchId).build();
        Branch branch = Branch.builder().id(branchId).name("Branch B").build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(productId.toString());
        when(serverRequest.queryParam("branchId")).thenReturn(java.util.Optional.of(branchId.toString()));
        when(serverRequest.bodyToMono(UpdateProductNameRequest.class)).thenReturn(Mono.just(updateRequest));
        when(productUseCase.updateName(productId, branchId, "Updated Product Name")).thenReturn(Mono.just(updatedProduct));
        when(branchRepository.findById(branchId)).thenReturn(Mono.just(branch));

        Mono<ServerResponse> responseMono = productHandler.updateName(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(productUseCase).updateName(productId, branchId, "Updated Product Name");
        verify(branchRepository).findById(branchId);
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        Integer productId = 1;
        Integer branchId = 2;
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(productId.toString());
        when(serverRequest.queryParam("branchId")).thenReturn(java.util.Optional.of(branchId.toString()));
        when(productUseCase.delete(productId, branchId)).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = productHandler.delete(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(NO_CONTENT))
                .verifyComplete();

        verify(productUseCase).delete(productId, branchId);
    }
}