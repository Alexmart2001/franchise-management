package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Branch> save(Branch branch);
    Mono<Branch> updateName(Integer id, String newName);
    Mono<Branch> findById(Integer id);
    Flux<Branch> findByFranchiseId(Integer franchiseId);
}
