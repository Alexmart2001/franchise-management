package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.ProductRequest;
import co.com.bancolombia.api.dto.ProductResponse;
import co.com.bancolombia.api.dto.UpdateProductNameRequest;
import co.com.bancolombia.api.dto.UpdateProductStockRequest;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .map(dto -> Product.builder()
                        .name(dto.getName())
                        .stock(dto.getStock())
                        .branchId(dto.getBranchId())
                        .build()
                )
                .flatMap(productUseCase::create)
                .map(this::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .doOnError(ex -> log.error("Error creating product", ex));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return productUseCase.findById(id)
                .map(this::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error finding product by id: {}", id, ex));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(UpdateProductNameRequest.class)
                .flatMap(dto -> productUseCase.updateName(id, dto.getName()))
                .map(this::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error updating product name for id: {}", id, ex));
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(UpdateProductStockRequest.class)
                .flatMap(dto -> productUseCase.updateStock(id, dto.getStock()))
                .map(this::toResponse)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error updating product stock for id: {}", id, ex));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return productUseCase.delete(id)
                .then(ServerResponse.noContent().build())
                .doOnError(ex -> log.error("Error deleting product by id: {}", id, ex));
    }

    public Mono<ServerResponse> findMaxStockByFranchise(ServerRequest request) {
        Integer franchiseId = Integer.valueOf(request.pathVariable("franchiseId"));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        productUseCase.findMaxStockByFranchise(franchiseId)
                                .map(this::toResponse),
                        ProductResponse.class
                )
                .doOnError(ex -> log.error("Error finding max stock products by franchiseId: {}", franchiseId, ex));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }
}
