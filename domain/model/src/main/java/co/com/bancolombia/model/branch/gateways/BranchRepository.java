package co.com.bancolombia.model.branch.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Product> save(Product product);
    Mono<Void> deleteById(Integer id);
    Mono<Product> updateStock(Integer id, Integer newStock);
    Mono<Product> updateName(Integer id, String newName);
    Flux<Product> findByBranchId(Integer branchId);
    Flux<Product> findMaxStockByFranchise(Integer franchiseId);
}
