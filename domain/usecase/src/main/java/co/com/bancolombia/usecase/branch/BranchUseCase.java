package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.commons.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;
    private final ProductRepository productRepository;

    public Mono<Branch> create(Branch branch) {
        return validateBranch(branch)
                .then(validateFranchiseExists(branch.getFranchiseId()))
                .then(branchRepository.save(branch));
    }

    public Mono<Branch> updateName(Integer id, String newName) {
        return validateId(id)
                .then(validateName(newName))
                .then(findOrThrow(id))
                .map(branch -> branch.toBuilder()
                        .name(newName)
                        .build())
                .flatMap(branchRepository::save);
    }

    public Mono<Void> delete(Integer id) {
        return validateId(id)
                .then(findOrThrow(id))
                .then(validateBranchHasNoProducts(id))
                .then(branchRepository.deleteById(id));
    }

    public Mono<Branch> findById(Integer id) {
        return validateId(id)
                .then(findOrThrow(id));
    }

    public Flux<Branch> findByFranchiseId(Integer franchiseId) {
        return validateId(franchiseId)
                .thenMany(branchRepository.findByFranchiseId(franchiseId));
    }

    private Mono<Branch> findOrThrow(Integer id) {
        return branchRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new BusinessException("BRANCH_NOT_FOUND", "Branch not found")));
    }

    private Mono<Void> validateFranchiseExists(Integer franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new BusinessException("FRANCHISE_NOT_FOUND", "Franchise not found")))
                .then();
    }

    private Mono<Void> validateBranchHasNoProducts(Integer branchId) {
        return productRepository.findByBranchId(branchId)
                .hasElements()
                .flatMap(hasProducts -> {
                    if (hasProducts)
                        return Mono.error(new BusinessException(
                                "BRANCH_HAS_PRODUCTS", "Cannot delete branch with products"));
                    return Mono.empty();
                });
    }

    private Mono<Void> validateBranch(Branch branch) {
        return Mono.defer(() -> {
            if (branch == null)
                return Mono.error(new BusinessException("BRANCH_NULL", "Branch cannot be null"));

            if (branch.getName() == null || branch.getName().isBlank())
                return Mono.error(new BusinessException("INVALID_NAME", "Name cannot be empty"));

            if (branch.getFranchiseId() == null || branch.getFranchiseId() <= 0)
                return Mono.error(new BusinessException("INVALID_FRANCHISE", "Invalid franchise id"));

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