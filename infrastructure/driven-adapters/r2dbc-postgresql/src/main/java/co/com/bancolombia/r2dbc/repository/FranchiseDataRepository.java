package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseDataRepository extends ReactiveCrudRepository<FranchiseEntity, Integer> {
}