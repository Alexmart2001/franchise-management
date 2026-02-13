package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductDataRepository extends ReactiveCrudRepository<ProductEntity, Integer> {
    Flux<ProductEntity> findByBranchId(Integer branchId);

    Flux<ProductEntity> findTopByBranchIdOrderByStockDesc(Integer branchId);
}