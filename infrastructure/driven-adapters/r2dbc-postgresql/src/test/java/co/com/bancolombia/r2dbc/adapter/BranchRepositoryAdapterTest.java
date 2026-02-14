package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.r2dbc.entity.BranchEntity;
import co.com.bancolombia.r2dbc.repository.BranchDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class BranchRepositoryAdapterTest {

    @Mock
    private BranchDataRepository branchDataRepository;

    @InjectMocks
    private BranchRepositoryAdapter branchRepositoryAdapter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_ValidBranch_ShouldSaveSuccessfully() {
        Branch branch = Branch.builder()
                .id(1)
                .name("Main Branch")
                .franchiseId(101)
                .build();

        BranchEntity branchEntity = BranchEntity.fromDomain(branch);

        when(branchDataRepository.save(branchEntity))
                .thenReturn(Mono.just(branchEntity));

        StepVerifier.create(branchRepositoryAdapter.save(branch))
                .expectNextMatches(savedBranch -> savedBranch.getId().equals(branch.getId()) &&
                        "Main Branch".equals(savedBranch.getName()))
                .verifyComplete();

        verify(branchDataRepository, times(1)).save(branchEntity);
    }

    @Test
    void testFindById_ExistingBranch_ShouldReturnBranch() {
        Integer branchId = 1;

        BranchEntity branchEntity = BranchEntity.builder()
                .id(branchId)
                .name("Main Branch")
                .franchiseId(101)
                .build();

        when(branchDataRepository.findById(branchId))
                .thenReturn(Mono.just(branchEntity));

        StepVerifier.create(branchRepositoryAdapter.findById(branchId))
                .expectNextMatches(branch -> branch.getId().equals(branchId) &&
                        "Main Branch".equals(branch.getName()))
                .verifyComplete();

        verify(branchDataRepository, times(1)).findById(branchId);
    }

    @Test
    void testFindById_NonExistingBranch_ShouldReturnEmptyMono() {
        Integer branchId = 2;

        when(branchDataRepository.findById(branchId)).thenReturn(Mono.empty());

        StepVerifier.create(branchRepositoryAdapter.findById(branchId))
                .verifyComplete();

        verify(branchDataRepository, times(1)).findById(branchId);
    }

    @Test
    void testFindByFranchiseId_ShouldReturnBranches() {
        Integer franchiseId = 101;

        BranchEntity branchEntity1 = BranchEntity.builder()
                .id(1)
                .name("Branch A")
                .franchiseId(franchiseId)
                .build();

        BranchEntity branchEntity2 = BranchEntity.builder()
                .id(2)
                .name("Branch B")
                .franchiseId(franchiseId)
                .build();

        when(branchDataRepository.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branchEntity1, branchEntity2));

        StepVerifier.create(branchRepositoryAdapter.findByFranchiseId(franchiseId))
                .expectNextMatches(branch -> "Branch A".equals(branch.getName()))
                .expectNextMatches(branch -> "Branch B".equals(branch.getName()))
                .verifyComplete();

        verify(branchDataRepository, times(1)).findByFranchiseId(franchiseId);
    }

    @Test
    void testFindByFranchiseId_NoBranches_ShouldReturnEmptyFlux() {
        Integer franchiseId = 102;

        when(branchDataRepository.findByFranchiseId(franchiseId)).thenReturn(Flux.empty());

        StepVerifier.create(branchRepositoryAdapter.findByFranchiseId(franchiseId))
                .verifyComplete();

        verify(branchDataRepository, times(1)).findByFranchiseId(franchiseId);
    }
}