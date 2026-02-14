package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Flux<Product> findMaxStockByFranchise(Integer franchiseId);
    Mono<Product> findById(Integer id);
    Mono<Void> deleteByIdAndBranch (Integer productId, Integer branchId);
    Mono<Boolean> existsInBranch(Integer productId, Integer branchId);
    Mono<Product> findByIdAndBranch(Integer productId, Integer branchId);
}
