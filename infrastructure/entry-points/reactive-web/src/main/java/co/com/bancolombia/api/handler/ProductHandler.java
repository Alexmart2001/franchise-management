package co.com.bancolombia.api.handler;

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
        return request.bodyToMono(Product.class)
                .flatMap(product -> {
                    log.info("Received request to create product: {}", product);
                    return productUseCase.create(product);
                })
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product))
                .doOnError(ex -> log.error("Error creating product", ex));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        log.debug("Received request to find product by id: {}", id);
        return productUseCase.findById(id)
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product))
                        .
                switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error finding product by id: {}", id, ex));
    }

    public Mono<ServerResponse> findByBranchId(ServerRequest request) {
        Integer branchId = Integer.valueOf(request.pathVariable("branchId"));
        log.debug("Received request to find products by branchId: {}", branchId);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productUseCase.findByBranchId(branchId), Product.class)
                .doOnError(ex -> log.error("Error finding products by branchId: {}", branchId, ex));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return request.bodyToMono(String.class)
                .flatMap(newName -> productUseCase.updateName(id, newName))
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product))
                .doOnError(ex -> log.error("Error updating product name for id: {}", id, ex));
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return request.bodyToMono(Integer.class)
                .flatMap(newStock -> productUseCase.updateStock(id, newStock))
                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product))
                .doOnError(ex -> log.error("Error updating product stock for id: {}", id, ex));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        log.info("Received request to delete product by id: {}", id);
        return productUseCase.delete(id)
                .then(ServerResponse.noContent().build())
                .doOnError(ex -> log.error("Error deleting product by id: {}", id, ex));
    }

    public Mono<ServerResponse> findMaxStockByFranchise(ServerRequest request) {
        Integer franchiseId = Integer.valueOf(request.pathVariable("franchiseId"));
        log.debug("Received request to find max stock products by franchiseId: {}", franchiseId);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productUseCase.findMaxStockByFranchise(franchiseId), Product.class)
                .doOnError(ex -> log.error("Error finding max stock products by franchiseId: {}", franchiseId, ex));
    }
}