package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.BranchHandler;
import co.com.bancolombia.api.handler.FranchiseHandler;
import co.com.bancolombia.api.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(
            FranchiseHandler franchiseHandler,
            BranchHandler branchHandler,
            ProductHandler productHandler) {
        return RouterFunctions
                // Franchise endpoints
                .route(POST("/franchises"), franchiseHandler::create)
                .andRoute(GET("/franchises/{id}"), franchiseHandler::findById)
                .andRoute(PATCH("/franchises/{id}/name"), franchiseHandler::updateName)
                // Branch endpoints
                .andRoute(POST("/branches"), branchHandler::create)
                .andRoute(GET("/branches/{id}"), branchHandler::findById)
                .andRoute(GET("/franchises/{franchiseId}/branches"), branchHandler::findByFranchiseId)
                .andRoute(PATCH("/branches/{id}/name"), branchHandler::updateName)
                // Product endpoints
                .andRoute(POST("/products"), productHandler::create)
                .andRoute(GET("/products/{id}"), productHandler::findById)
                .andRoute(PATCH("/products/{id}/name"), productHandler::updateName)
                .andRoute(PATCH("/products/{id}/stock"), productHandler::updateStock)
                .andRoute(DELETE("/products/{id}"), productHandler::delete)
                .andRoute(GET("/franchises/{franchiseId}/max-stock-products"), productHandler::findMaxStockByFranchise);
    }
}