package co.com.bancolombia.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductResponse {
    Integer id;
    String name;
    Integer stock;
    Integer branchId;
}
