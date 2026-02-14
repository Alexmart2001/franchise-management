package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.repository.FranchiseDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseDataRepository franchiseDataRepository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        log.info("Saving franchise: {}", franchise);
        return franchiseDataRepository.save(FranchiseEntity.fromDomain(franchise))
                .doOnSuccess(entity -> log.info("Franchise saved: {}", entity))
                .doOnError(ex -> log.error("Error saving franchise: {}", franchise, ex))
                .map(FranchiseEntity::toDomain);
    }

    @Override
    public Mono<Franchise>
    findById(Integer id) {
        log.debug("Finding franchise by id: {}", id);
        return franchiseDataRepository.findById(id)
                .doOnSuccess(entity -> {
                    if (entity != null) log.info("Franchise found: {}", entity);
                    else log.warn("Franchise not found for id: {}", id);
                })
                .doOnError(ex -> log.error("Error finding franchise by id: {}", id, ex))
                .map(FranchiseEntity::toDomain);
    }
}