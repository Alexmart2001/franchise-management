package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.usecase.commons.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class FranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFranchise_ValidFranchise_ShouldSave() {
        // Arrange
        Franchise franchise = Franchise.builder()
                .id(1)
                .name("Valid Franchise")
                .build();

        when(franchiseRepository.save(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.create(franchise))
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void testUpdateFranchiseName_ValidData_ShouldUpdateAndSave() {
        Integer franchiseId = 1;
        String newName = "Updated Franchise Name";
        Franchise existingFranchise = Franchise.builder()
                .id(franchiseId)
                .name("Old Franchise Name")
                .build();

        Franchise updatedFranchise = Franchise.builder()
                .id(franchiseId)
                .name(newName)
                .build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.save(updatedFranchise)).thenReturn(Mono.just(updatedFranchise));

        StepVerifier.create(franchiseUseCase.updateName(franchiseId, newName))
                .expectNext(updatedFranchise)
                .verifyComplete();
    }

    @Test
    void testFindFranchiseById_ValidId_ShouldReturnFranchise() {
        Integer franchiseId = 1;
        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Valid Franchise")
                .build();

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.findById(franchiseId))
                .expectNext(franchise)
                .verifyComplete();
    }

    @Test
    void testFindFranchiseById_FranchiseNotFound_ShouldThrowError() {
        Integer franchiseId = 1;

        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.findById(franchiseId))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "FRANCHISE_NOT_FOUND".equals(((BusinessException) throwable).getCode()))
                .verify();
    }
}