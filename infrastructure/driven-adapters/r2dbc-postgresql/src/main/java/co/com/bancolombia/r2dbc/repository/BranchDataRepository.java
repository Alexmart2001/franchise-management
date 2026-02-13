package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BranchDataRepository extends ReactiveCrudRepository<BranchEntity, Integer> {
    Flux<BranchEntity> findByFranchiseId(Integer franchiseId);
}