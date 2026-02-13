package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.commons.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    public Mono<Product> create(Product product) {
        return validateProduct(product)
                .then(validateBranchExists(product.getBranchId()))
                .then(productRepository.save(product));
    }

    public Mono<Product> updateName(Integer id, String newName) {
        return validateId(id)
                .then(validateName(newName))
                .then(findOrThrow(id))
                .map(product -> product.toBuilder()
                        .name(newName)
                        .build())
                .flatMap(productRepository::save);
    }

    public Mono<Product> updateStock(Integer id, Integer newStock) {
        return validateId(id)
                .then(validateStock(newStock))
                .then(findOrThrow(id))
                .map(product -> product.toBuilder()
                        .stock(newStock)
                        .build())
                .flatMap(productRepository::save);
    }

    public Mono<Void> delete(Integer id) {
        return validateId(id)
                .then(findOrThrow(id))
                .flatMap(product -> productRepository.deleteById(id));
    }

    public Flux<Product> findMaxStockByFranchise(Integer franchiseId) {
        return validateId(franchiseId)
                .thenMany(productRepository.findMaxStockByFranchise(franchiseId));
    }

    public Mono<Product> findById(Integer id) {
        return validateId(id)
                .then(findOrThrow(id));
    }

    public Flux<Product> findByBranchId(Integer branchId) {
        return validateId(branchId)
                .thenMany(productRepository.findByBranchId(branchId));
    }

    private Mono<Product> findOrThrow(Integer id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new BusinessException("PRODUCT_NOT_FOUND", "Product not found")));
    }

    private Mono<Void> validateBranchExists(Integer branchId) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(
                        new BusinessException("BRANCH_NOT_FOUND", "Branch not found")))
                .then();
    }

    private Mono<Void> validateProduct(Product product) {
        return Mono.defer(() -> {
            if (product == null)
                return Mono.error(new BusinessException("PRODUCT_NULL", "Product cannot be null"));

            if (product.getName() == null || product.getName().isBlank())
                return Mono.error(new BusinessException("INVALID_NAME", "Name cannot be empty"));

            if (product.getStock() == null || product.getStock() < 0)
                return Mono.error(new BusinessException("INVALID_STOCK", "Stock cannot be negative"));

            if (product.getBranchId() == null || product.getBranchId() <= 0)
                return Mono.error(new BusinessException("INVALID_BRANCH", "Invalid branch id"));

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

    private Mono<Void> validateStock(Integer stock) {
        return Mono.defer(() -> {
            if (stock == null || stock < 0)
                return Mono.error(new BusinessException("INVALID_STOCK", "Stock cannot be negative"));
            return Mono.empty();
        });
    }
}