package co.com.bancolombia.api.handler;

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
        return request.bodyToMono(Franchise.class)
                .flatMap(franchise -> {
                    log.info("Received request to create franchise: {}", franchise);
                    return franchiseUseCase.create(franchise);
                })
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .doOnError(ex -> log.error("Error creating franchise", ex));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        log.debug("Received request to find franchise by id: {}", id);
        return franchiseUseCase.findById(id)
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error finding franchise by id: {}", id, ex));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {

        log.debug("Received request to find all franchises");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(franchiseUseCase.findAll(), Franchise.class)
                .doOnError(ex -> log.error("Error finding all franchises", ex));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return request.bodyToMono(String.class)
                .flatMap(newName -> franchiseUseCase.updateName(id, newName))
                .flatMap(franchise -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(franchise))
                .doOnError(ex -> log.error("Error updating franchise name for id: {}", id, ex));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        log.info("Received request to delete franchise by id: {}", id);
        return franchiseUseCase.delete(id)
                .then(ServerResponse.noContent().build())
                .doOnError(ex -> log.error("Error deleting franchise by id: {}", id, ex));
    }
}