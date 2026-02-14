package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.commons.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class BranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BranchUseCase branchUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateBranchName() {
        Integer id = 1;
        String newName = "New Office";
        Branch originalBranch = Branch.builder()
                .id(id)
                .name("Main Office")
                .franchiseId(10)
                .build();
        Branch updatedBranch = originalBranch.toBuilder()
                .name(newName)
                .build();

        given(branchRepository.findById(id)).willReturn(Mono.just(originalBranch));
        given(branchRepository.save(any(Branch.class))).willReturn(Mono.just(updatedBranch));

        StepVerifier.create(branchUseCase.updateName(id, newName))
                .expectNext(updatedBranch)
                .verifyComplete();

        verify(branchRepository, times(1)).findById(id);
        verify(branchRepository, times(1)).save(any(Branch.class));
    }

    @Test
    void testUpdateBranchName_InvalidName() {
        Integer id = 1;
        String newName = "";

        when(branchRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(branchUseCase.updateName(id, newName))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_NAME".equals(((BusinessException) throwable).getCode()))
                .verify();

        verify(branchRepository).findById(id);
        verify(branchRepository, never()).save(any(Branch.class));
    }

    @Test
    void testFindBranchById() {
        Integer id = 1;
        Branch branch = Branch.builder()
                .id(id)
                .name("Main Office")
                .franchiseId(10)
                .build();

        given(branchRepository.findById(id)).willReturn(Mono.just(branch));

        StepVerifier.create(branchUseCase.findById(id))
                .expectNext(branch)
                .verifyComplete();

        verify(branchRepository, times(1)).findById(id);
    }

    @Test
    void testFindBranchByFranchiseId() {
        Integer franchiseId = 10;
        Branch branch1 = Branch.builder()
                .id(1)
                .name("Office A")
                .franchiseId(franchiseId)
                .build();
        Branch branch2 = Branch.builder()
                .id(2)
                .name("Office B")
                .franchiseId(franchiseId)
                .build();

        given(branchRepository.findByFranchiseId(franchiseId)).willReturn(Flux.just(branch1, branch2));

        StepVerifier.create(branchUseCase.findByFranchiseId(franchiseId))
                .expectNext(branch1)
                .expectNext(branch2)
                .verifyComplete();

        verify(branchRepository, times(1)).findByFranchiseId(franchiseId);
    }
    @Test
    void validateFranchiseExists_FranchiseNotFound_ShouldThrowError() throws Exception {
        Integer franchiseId = -1;
        when(franchiseRepository.findById(franchiseId)).thenReturn(Mono.empty());

        Method validateFranchiseExistsMethod = BranchUseCase.class.getDeclaredMethod("validateFranchiseExists", Integer.class);
        validateFranchiseExistsMethod.setAccessible(true);

        Mono<Void> result = (Mono<Void>) validateFranchiseExistsMethod.invoke(branchUseCase, franchiseId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "FRANCHISE_NOT_FOUND".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateBranch_BranchNull_ShouldThrowError() throws Exception {
        Method validateBranchMethod = BranchUseCase.class.getDeclaredMethod("validateBranch", Branch.class);
        validateBranchMethod.setAccessible(true);

        Mono<Void> result = (Mono<Void>) validateBranchMethod.invoke(branchUseCase, (Branch) null);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "BRANCH_NULL".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateBranch_InvalidName_ShouldThrowError() throws Exception {
        Branch branch = Branch.builder()
                .id(1)
                .name("")
                .franchiseId(1)
                .build();

        Method validateBranchMethod = BranchUseCase.class.getDeclaredMethod("validateBranch", Branch.class);
        validateBranchMethod.setAccessible(true);

        Mono<Void> result = (Mono<Void>) validateBranchMethod.invoke(branchUseCase, branch);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_NAME".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateBranch_InvalidFranchiseId_ShouldThrowError() throws Exception {
        Branch branch = Branch.builder()
                .id(1)
                .name("Valid Branch")
                .franchiseId(-1)
                .build();

        Method validateBranchMethod = BranchUseCase.class.getDeclaredMethod("validateBranch", Branch.class);
        validateBranchMethod.setAccessible(true);

        Mono<Void> result = (Mono<Void>) validateBranchMethod.invoke(branchUseCase, branch);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        "INVALID_FRANCHISE".equals(((BusinessException) throwable).getCode()))
                .verify();
    }

    @Test
    void validateBranch_ValidBranch_ShouldComplete() throws Exception {
        Branch branch = Branch.builder()
                .id(1)
                .name("Valid Branch")
                .franchiseId(1)
                .build();

        Method validateBranchMethod = BranchUseCase.class.getDeclaredMethod("validateBranch", Branch.class);
        validateBranchMethod.setAccessible(true);

        Mono<Void> result = (Mono<Void>) validateBranchMethod.invoke(branchUseCase, branch);

        StepVerifier.create(result)
                .verifyComplete();
    }
}