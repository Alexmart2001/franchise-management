package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.FranchiseRequest;
import co.com.bancolombia.api.dto.FranchiseResponse;
import co.com.bancolombia.api.dto.UpdateFranchiseNameRequest;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .map(dto -> Franchise.builder()
                        .name(dto.getName())
                        .build()
                )
                .flatMap(franchiseUseCase::create)
                .map(franchise -> FranchiseResponse.builder()
                        .id(franchise.getId())
                        .name(franchise.getName())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .doOnError(ex -> log.error("Error creating franchise", ex));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return franchiseUseCase.findById(id)
                .map(franchise -> FranchiseResponse.builder()
                        .id(franchise.getId())
                        .name(franchise.getName())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error finding franchise by id: {}", id, ex));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        franchiseUseCase.findAll()
                                .map(franchise -> FranchiseResponse.builder()
                                        .id(franchise.getId())
                                        .name(franchise.getName())
                                        .build()
                                ),
                        FranchiseResponse.class
                )
                .doOnError(ex -> log.error("Error finding all franchises", ex));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(UpdateFranchiseNameRequest.class)
                .flatMap(dto -> franchiseUseCase.updateName(id, dto.getName()))
                .map(franchise -> FranchiseResponse.builder()
                        .id(franchise.getId())
                        .name(franchise.getName())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error updating franchise name for id: {}", id, ex));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return franchiseUseCase.delete(id)
                .then(ServerResponse.noContent().build())
                .doOnError(ex -> log.error("Error deleting franchise by id: {}", id, ex));
    }
}
