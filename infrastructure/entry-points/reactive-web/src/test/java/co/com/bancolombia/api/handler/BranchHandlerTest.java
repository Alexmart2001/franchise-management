package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.BranchRequest;
import co.com.bancolombia.api.dto.UpdateBranchNameRequest;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class BranchHandlerTest {

    @Mock
    private BranchUseCase branchUseCase;

    @InjectMocks
    private BranchHandler branchHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateBranchSuccessfully() {
        BranchRequest request = new BranchRequest("Branch A", 1); // DTO de entrada
        Branch branch = Branch.builder().id(1).name("Branch A").franchiseId(1).build(); // Dominio
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.bodyToMono(BranchRequest.class)).thenReturn(Mono.just(request));
        when(branchUseCase.create(any(Branch.class))).thenReturn(Mono.just(branch));

        Mono<ServerResponse> responseMono = branchHandler.create(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(branchUseCase).create(any(Branch.class));
    }

    @Test
    void shouldFindBranchByIdSuccessfully() {
        Integer branchId = 1;
        Branch branch = Branch.builder().id(branchId).name("Branch A").franchiseId(1).build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(branchId.toString());
        when(branchUseCase.findById(branchId)).thenReturn(Mono.just(branch));

        Mono<ServerResponse> responseMono = branchHandler.findById(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(branchUseCase).findById(branchId);
    }

    @Test
    void shouldReturnNotFoundWhenBranchDoesNotExistById() {
        Integer branchId = 999;
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(branchId.toString());
        when(branchUseCase.findById(branchId)).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = branchHandler.findById(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(NOT_FOUND))
                .verifyComplete();

        verify(branchUseCase).findById(branchId);
    }

    @Test
    void shouldFindBranchesByFranchiseIdSuccessfully() {
        Integer franchiseId = 1;
        Branch branch1 = Branch.builder().id(1).name("Branch A").franchiseId(franchiseId).build();
        Branch branch2 = Branch.builder().id(2).name("Branch B").franchiseId(franchiseId).build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("franchiseId")).thenReturn(franchiseId.toString());
        when(branchUseCase.findByFranchiseId(franchiseId)).thenReturn(Flux.just(branch1, branch2));

        Mono<ServerResponse> responseMono = branchHandler.findByFranchiseId(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(branchUseCase).findByFranchiseId(franchiseId);
    }

    @Test
    void shouldUpdateBranchNameSuccessfully() {
        Integer branchId = 1;
        UpdateBranchNameRequest updateRequest = new UpdateBranchNameRequest("Updated Branch Name");
        Branch updatedBranch = Branch.builder().id(branchId).name("Updated Branch Name").franchiseId(1).build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(branchId.toString());
        when(serverRequest.bodyToMono(UpdateBranchNameRequest.class)).thenReturn(Mono.just(updateRequest));
        when(branchUseCase.updateName(branchId, updateRequest.getName())).thenReturn(Mono.just(updatedBranch));

        Mono<ServerResponse> responseMono = branchHandler.updateName(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(branchUseCase).updateName(branchId, "Updated Branch Name");
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNameAndBranchDoesNotExist() {
        Integer branchId = 999;
        UpdateBranchNameRequest updateRequest = new UpdateBranchNameRequest("Updated Branch Name");
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(branchId.toString());
        when(serverRequest.bodyToMono(UpdateBranchNameRequest.class)).thenReturn(Mono.just(updateRequest));
        when(branchUseCase.updateName(branchId, updateRequest.getName())).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = branchHandler.updateName(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(NOT_FOUND))
                .verifyComplete();

        verify(branchUseCase).updateName(branchId, "Updated Branch Name");
    }
}