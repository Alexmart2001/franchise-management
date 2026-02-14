package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.r2dbc.entity.BranchEntity;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class BranchRepositoryAdapter implements BranchRepository {

    private final BranchDataRepository branchDataRepository;

    @Override
    public Mono<Branch> save(Branch branch) {
        log.info("Saving branch: {}", branch);
        return branchDataRepository.save(BranchEntity.fromDomain(branch))
                .doOnSuccess(entity -> log.info("Branch saved: {}", entity))
                .doOnError(ex -> log.error("Error saving branch: {}", branch, ex))
                .map(BranchEntity::toDomain);
    }

    @Override
    public Mono<Branch> findById(Integer id) {
        log.debug("Finding branch by id: {}", id);
        return branchDataRepository.findById(id)
                .doOnSuccess(entity -> {
                    if (entity != null) log.info("Branch found: {}", entity);
                    else log.warn("Branch not found for id: {}", id);
                })
                .doOnError(ex -> log.error("Error finding branch by id: {}", id, ex))
                .map(BranchEntity::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(Integer franchiseId) {
        log.debug("Finding branches by franchiseId: {}", franchiseId);
        return branchDataRepository.findByFranchiseId(franchiseId)
                .doOnComplete(() -> log.info("Completed finding branches for franchiseId: {}", franchiseId))
                .doOnError(ex -> log.error("Error finding branches for franchiseId: {}", franchiseId, ex))
                .map(BranchEntity::toDomain);
    }
}