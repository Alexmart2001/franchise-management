package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.FranchiseRequest;
import co.com.bancolombia.api.dto.UpdateFranchiseNameRequest;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class FranchiseHandlerTest {

    @Mock
    private FranchiseUseCase franchiseUseCase;

    @InjectMocks
    private FranchiseHandler franchiseHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {
        FranchiseRequest request = new FranchiseRequest("Franchise A");
        Franchise franchise = Franchise.builder().id(1).name("Franchise A").build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.bodyToMono(FranchiseRequest.class)).thenReturn(Mono.just(request));
        when(franchiseUseCase.create(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<ServerResponse> responseMono = franchiseHandler.create(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(franchiseUseCase).create(any(Franchise.class));
    }

    @Test
    void shouldFindFranchiseByIdSuccessfully() {
        Integer franchiseId = 1;
        Franchise franchise = Franchise.builder().id(franchiseId).name("Franchise A").build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(franchiseId.toString());
        when(franchiseUseCase.findById(franchiseId)).thenReturn(Mono.just(franchise));

        Mono<ServerResponse> responseMono = franchiseHandler.findById(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(franchiseUseCase).findById(franchiseId);
    }

    @Test
    void shouldReturnNotFoundWhenFranchiseDoesNotExistById() {
        Integer franchiseId = 999;
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(franchiseId.toString());
        when(franchiseUseCase.findById(franchiseId)).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = franchiseHandler.findById(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(NOT_FOUND))
                .verifyComplete();

        verify(franchiseUseCase).findById(franchiseId);
    }

    @Test
    void shouldUpdateFranchiseNameSuccessfully() {
        Integer franchiseId = 1;
        UpdateFranchiseNameRequest updateRequest = new UpdateFranchiseNameRequest("Updated Franchise Name");
        Franchise updatedFranchise = Franchise.builder().id(franchiseId).name("Updated Franchise Name").build();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(franchiseId.toString());
        when(serverRequest.bodyToMono(UpdateFranchiseNameRequest.class)).thenReturn(Mono.just(updateRequest));
        when(franchiseUseCase.updateName(franchiseId, updateRequest.getName())).thenReturn(Mono.just(updatedFranchise));

        Mono<ServerResponse> responseMono = franchiseHandler.updateName(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(OK))
                .verifyComplete();

        verify(franchiseUseCase).updateName(franchiseId, "Updated Franchise Name");
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNameAndFranchiseDoesNotExist() {
        Integer franchiseId = 999;
        UpdateFranchiseNameRequest updateRequest = new UpdateFranchiseNameRequest("Updated Franchise Name");
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("id")).thenReturn(franchiseId.toString());
        when(serverRequest.bodyToMono(UpdateFranchiseNameRequest.class)).thenReturn(Mono.just(updateRequest));
        when(franchiseUseCase.updateName(franchiseId, updateRequest.getName())).thenReturn(Mono.empty());

        Mono<ServerResponse> responseMono = franchiseHandler.updateName(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(NOT_FOUND))
                .verifyComplete();

        verify(franchiseUseCase).updateName(franchiseId, "Updated Franchise Name");
    }
}