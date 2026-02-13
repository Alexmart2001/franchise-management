package co.com.bancolombia.api.handler;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.usecase.branch.BranchUseCase;
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
public class BranchHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Branch.class)
                .flatMap(branch -> {
                    log.info("Received request to create branch: {}", branch);
                    return branchUseCase.create(branch);
                })
                .flatMap(branch -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(branch))
                .doOnError(ex -> log.error("Error creating branch", ex));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        log.debug("Received request to find branch by id: {}", id);
        return branchUseCase.findById(id)
                .flatMap(branch -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(branch))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error finding branch by id: {}", id, ex));
    }

    public Mono<ServerResponse> findByFranchiseId(ServerRequest request) {
        Integer franchiseId = Integer.valueOf(request.pathVariable("franchiseId"));
        log.debug("Received request to find branches by franchiseId: {}", franchiseId);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(branchUseCase.findByFranchiseId(franchiseId), Branch.class)
                .doOnError(ex -> log.error("Error finding branches by franchiseId: {}", franchiseId, ex));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return request.bodyToMono(String.class)
                .flatMap(newName -> branchUseCase.updateName(id, newName))
                .flatMap(branch -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(branch))
                .doOnError(ex -> log.error("Error updating branch name for id: {}", id, ex));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        log.info("Received request to delete branch by id: {}", id);
        return branchUseCase.delete(id)
                .then(ServerResponse.noContent().build())
                .doOnError(ex -> log.error("Error deleting branch by id: {}", id, ex));
    }
}