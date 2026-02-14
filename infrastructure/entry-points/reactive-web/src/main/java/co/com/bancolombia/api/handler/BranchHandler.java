package co.com.bancolombia.api.handler;

import co.com.bancolombia.api.dto.BranchRequest;
import co.com.bancolombia.api.dto.BranchResponse;
import co.com.bancolombia.api.dto.UpdateBranchNameRequest;
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
        return request.bodyToMono(BranchRequest.class)
                .map(dto -> Branch.builder()
                        .name(dto.getName())
                        .franchiseId(dto.getFranchiseId())
                        .build()
                )
                .flatMap(branchUseCase::create)
                .map(branch -> BranchResponse.builder()
                        .id(branch.getId())
                        .name(branch.getName())
                        .franchiseId(branch.getFranchiseId())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .doOnError(ex -> log.error("Error creating branch", ex));
    }

    /**
     * Endpoint para agregar una nueva sucursal a una franquicia.
     * Requisito obligatorio según la prueba.
     */
    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return branchUseCase.findById(id)
                .map(branch -> BranchResponse.builder()
                        .id(branch.getId())
                        .name(branch.getName())
                        .franchiseId(branch.getFranchiseId())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error finding branch by id: {}", id, ex));
    }

    public Mono<ServerResponse> findByFranchiseId(ServerRequest request) {
        Integer franchiseId = Integer.valueOf(request.pathVariable("franchiseId"));

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        branchUseCase.findByFranchiseId(franchiseId)
                                .map(branch -> BranchResponse.builder()
                                        .id(branch.getId())
                                        .name(branch.getName())
                                        .franchiseId(branch.getFranchiseId())
                                        .build()
                                ),
                        BranchResponse.class
                )
                .doOnError(ex -> log.error("Error finding branches by franchiseId: {}", franchiseId, ex));
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));

        return request.bodyToMono(UpdateBranchNameRequest.class)
                .flatMap(dto -> branchUseCase.updateName(id, dto.getName()))
                .map(branch -> BranchResponse.builder()
                        .id(branch.getId())
                        .name(branch.getName())
                        .franchiseId(branch.getFranchiseId())
                        .build()
                )
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build())
                .doOnError(ex -> log.error("Error updating branch name for id: {}", id, ex));
    }
}
