package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.repository.ProductDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductDataRepository productDataRepository;

    @Override
    public Mono<Product> save(Product product) {
        log.info("Saving product: {}", product);
        return productDataRepository.save(ProductEntity.fromDomain(product))
                .doOnSuccess(entity -> log.info("Product saved: {}", entity))
                .doOnError(ex -> log.error("Error saving product: {}", product, ex))
                .map(ProductEntity::toDomain);
    }

    @Override
    public Mono<Product> findById(Integer id) {
        log.debug("Finding product by id: {}", id);
        return productDataRepository.findById(id)
                .doOnSuccess(entity -> {
                    if (entity != null) log.info("Product found: {}", entity);
                    else log.warn("Product not found for id: {}", id);
                })
                .doOnError(ex -> log.error("Error finding product by id: {}", id, ex))
                .map(ProductEntity::toDomain);
    }

    @Override
    public Flux<Product> findMaxStockByFranchise(Integer franchiseId) {
        log.debug("Finding max stock products by franchiseId: {}", franchiseId);
        return productDataRepository.findMaxStockByFranchise(franchiseId)
                .doOnComplete(() -> log.info("Completed finding max stock products for franchiseId: {}", franchiseId))
                .doOnError(ex -> log.error("Error finding max stock products for franchiseId: {}", franchiseId, ex))
                .map(ProductEntity::toDomain);
    }

    @Override
    public Mono<Void> deleteByIdAndBranch(Integer productId, Integer branchId) {
        log.info("Deleting product: productId={} branchId={}", productId, branchId);
        return existsInBranch(productId, branchId)
                .flatMap(exists -> {
                    if (exists) {
                        return productDataRepository.deleteById(productId)
                                .doOnSuccess(v -> log.info("Product {} deleted from branch {}", productId, branchId))
                                .doOnError(ex -> log.error("Error deleting product {} from branch {}", productId, branchId, ex));
                    } else {
                        log.warn("Attempted to delete product {} that does not belong to branch {}", productId, branchId);
                        return Mono.error(new RuntimeException("Product does not belong to the specified branch."));
                    }
                });
    }
    @Override
    public Mono<Boolean> existsInBranch(Integer productId, Integer branchId) {
        log.debug("Verifying product exists in branch: productId={} branchId={}", productId, branchId);
        return productDataRepository.findById(productId)
                .filter(productEntity -> productEntity.getBranchId().equals(branchId))
                .hasElement()
                .doOnSuccess(exists -> {
                    if (Boolean.TRUE.equals(exists)) log.info("Product {} exists in branch {}", productId, branchId);
                    else log.warn("Product {} does not exist in branch {}", productId, branchId);
                })
                .doOnError(ex -> log.error("Error verifying product exists in branch: productId={} branchId={}", productId, branchId, ex));
    }

    @Override
    public Mono<Product> findByIdAndBranch(Integer productId, Integer branchId) {
        log.debug("Finding product by id={} and branchId={}", productId, branchId);
        return productDataRepository.findByIdAndBranch(productId, branchId)
                .doOnSuccess(entity -> {
                    if (entity != null) {
                        log.info("Product found: {}", entity);
                    } else {
                        log.warn("Product not found for id={} in branchId={}", productId, branchId);
                    }
                })
                .doOnError(ex -> log.error("Error finding product by id={} and branchId={}", productId, branchId, ex))
                .map(ProductEntity::toDomain);
    }
}