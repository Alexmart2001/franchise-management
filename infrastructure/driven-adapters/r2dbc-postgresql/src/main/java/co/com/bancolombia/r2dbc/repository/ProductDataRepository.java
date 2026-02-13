package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductDataRepository extends ReactiveCrudRepository<ProductEntity, Integer> {
    Flux<ProductEntity> findByBranchId(Integer branchId);

    @Query("""
        SELECT p.*
        FROM product p
        JOIN branch b ON p.branch_id = b.id
        WHERE b.franchise_id = :franchiseId
          AND p.stock = (
            SELECT MAX(p2.stock)
            FROM product p2
            WHERE p2.branch_id = p.branch_id
          )
    """)
    Flux<ProductEntity> findMaxStockByFranchise(Integer franchiseId);
}