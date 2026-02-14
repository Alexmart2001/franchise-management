package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.usecase.commons.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;

    public Mono<Franchise> create(Franchise franchise) {
        return validateFranchise(franchise)
                .then(franchiseRepository.save(franchise));
    }

    public Mono<Franchise> updateName(Integer id, String newName) {
        return validateId(id)
                .then(validateName(newName))
                .then(findOrThrow(id))
                .map(franchise -> franchise.toBuilder()
                        .name(newName)
                        .build())
                .flatMap(franchiseRepository::save);
    }

    public Mono<Franchise> findById(Integer id) {
        return validateId(id)
                .then(findOrThrow(id));
    }

    private Mono<Franchise> findOrThrow(Integer id) {
        return franchiseRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new BusinessException("FRANCHISE_NOT_FOUND", "Franchise not found")));
    }

    private Mono<Void> validateFranchise(Franchise franchise) {
        return Mono.defer(() -> {
            if (franchise == null)
                return Mono.error(new BusinessException("FRANCHISE_NULL", "Franchise cannot be null"));

            if (franchise.getName() == null || franchise.getName().isBlank())
                return Mono.error(new BusinessException("INVALID_NAME", "Name cannot be empty"));

            return Mono.empty();
        });
    }

    private Mono<Void> validateId(Integer id) {
        return Mono.defer(() -> {
            if (id == null || id <= 0)
                return Mono.error(new BusinessException("INVALID_ID", "Invalid id"));
            return Mono.empty();
        });
    }

    private Mono<Void> validateName(String name) {
        return Mono.defer(() -> {
            if (name == null || name.isBlank())
                return Mono.error(new BusinessException("INVALID_NAME", "Name cannot be empty"));
            return Mono.empty();
        });
    }
}