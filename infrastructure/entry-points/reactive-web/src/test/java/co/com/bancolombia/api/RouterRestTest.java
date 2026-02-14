package co.com.bancolombia.api;

import co.com.bancolombia.api.handler.BranchHandler;
import co.com.bancolombia.api.handler.FranchiseHandler;
import co.com.bancolombia.api.handler.ProductHandler;
import co.com.bancolombia.api.router.RouterRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;

@ContextConfiguration(classes = RouterRestTest.TestConfig.class)
class RouterRestTest {

    private WebTestClient webTestClient;

    private FranchiseHandler franchiseHandlerMock;
    private BranchHandler branchHandlerMock;
    private ProductHandler productHandlerMock;

    @BeforeEach
    void setUp() {
        franchiseHandlerMock = Mockito.mock(FranchiseHandler.class);
        branchHandlerMock = Mockito.mock(BranchHandler.class);
        productHandlerMock = Mockito.mock(ProductHandler.class);

        RouterRest routerRest = new RouterRest();

        webTestClient = WebTestClient.bindToRouterFunction(
                routerRest.routerFunction(franchiseHandlerMock, branchHandlerMock, productHandlerMock)
        ).build();

        configureMocks();
    }

    private void configureMocks() {
        Mockito.when(franchiseHandlerMock.create(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(franchiseHandlerMock.updateName(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(branchHandlerMock.create(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(branchHandlerMock.updateName(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(productHandlerMock.create(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(productHandlerMock.updateName(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(productHandlerMock.updateStock(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(productHandlerMock.delete(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(productHandlerMock.findMaxStockByFranchise(Mockito.any()))
                .thenReturn(Mono.empty());
    }

    @Test
    void shouldReturnOkForFranchiseCreateEndpoint() {
        webTestClient.post()
                .uri("/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Franchise A\"}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnOkForUpdateFranchiseNameEndpoint() {
        webTestClient.patch()
                .uri("/franchises/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Updated Franchise Name\"}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnOkForBranchCreateEndpoint() {
        webTestClient.post()
                .uri("/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Branch A\", \"franchiseId\": 1}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnOkForUpdateBranchNameEndpoint() {
        webTestClient.patch()
                .uri("/branches/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Updated Branch Name\"}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnOkForProductCreateEndpoint() {
        webTestClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Product A\", \"stock\": 10, \"branchId\": 1}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnOkForUpdateProductNameEndpoint() {
        webTestClient.patch()
                .uri("/products/1/name")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"Updated Product Name\"}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnOkForUpdateProductStockEndpoint() {
        webTestClient.patch()
                .uri("/products/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"stock\": 20}")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnOkForFindMaxStockProductsByFranchiseEndpoint() {
        webTestClient.get()
                .uri("/franchises/1/max-stock-products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Configuration
    public static class TestConfig {
        @Bean
        public RouterRest routerRest() {
            return new RouterRest();
        }
    }
}