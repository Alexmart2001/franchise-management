package co.com.bancolombia.api.config;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import co.com.bancolombia.usecase.product.ProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public FranchiseUseCase franchiseUseCase(FranchiseRepository franchiseRepository, BranchRepository branchRepository) {
        return new FranchiseUseCase(franchiseRepository, branchRepository);
    }

    @Bean
    public BranchUseCase branchUseCase(BranchRepository branchRepository, FranchiseRepository franchiseRepository, ProductRepository productRepository) {
        return new BranchUseCase(branchRepository, franchiseRepository, productRepository);
    }

    @Bean
    public ProductUseCase productUseCase(ProductRepository productRepository, BranchRepository branchRepository) {
        return new ProductUseCase(productRepository, branchRepository);
    }
}