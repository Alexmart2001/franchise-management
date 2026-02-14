package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.repository.FranchiseDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class FranchiseRepositoryAdapterTest {

    @Mock
    private FranchiseDataRepository franchiseDataRepository;

    @InjectMocks
    private FranchiseRepositoryAdapter franchiseRepositoryAdapter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_ValidFranchise_ShouldSaveSuccessfully() {
        Franchise franchise = Franchise.builder()
                .id(1)
                .name("Franchise A")
                .build();

        FranchiseEntity franchiseEntity = FranchiseEntity.builder()
                .id(1)
                .name("Franchise A")
                .build();

        when(franchiseDataRepository.save(FranchiseEntity.fromDomain(franchise)))
                .thenReturn(Mono.just(franchiseEntity));

        StepVerifier.create(franchiseRepositoryAdapter.save(franchise))
                .expectNextMatches(savedFranchise -> savedFranchise.getId().equals(franchise.getId()) &&
                        "Franchise A".equals(savedFranchise.getName()))
                .verifyComplete();
    }

    @Test
    void testFindById_ValidId_ShouldReturnFranchise() {
        Integer franchiseId = 1;

        FranchiseEntity franchiseEntity = FranchiseEntity.builder()
                .id(1)
                .name("Franchise A")
                .build();

        when(franchiseDataRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchiseEntity));

        StepVerifier.create(franchiseRepositoryAdapter.findById(franchiseId))
                .expectNextMatches(franchise -> franchise.getId().equals(franchiseId) &&
                        "Franchise A".equals(franchise.getName()))
                .verifyComplete();
    }

    @Test
    void testFindById_NonExistingFranchise_ShouldReturnEmpty() {
        Integer franchiseId = 2;

        when(franchiseDataRepository.findById(franchiseId)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseRepositoryAdapter.findById(franchiseId))
                .verifyComplete();
    }
}