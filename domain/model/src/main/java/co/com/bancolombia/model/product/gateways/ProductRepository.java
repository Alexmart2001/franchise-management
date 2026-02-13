package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Void> deleteById(Integer id);
    Flux<Product> findByBranchId(Integer branchId);
    Flux<Product> findMaxStockByFranchise(Integer franchiseId);
    Mono<Product> findById(Integer id);
}
