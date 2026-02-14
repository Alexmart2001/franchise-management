package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    Integer id;
    String name;
    Integer stock;
    Integer branchId;
    String branchName;
}
