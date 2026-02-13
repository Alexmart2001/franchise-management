package co.com.bancolombia.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FranchiseResponse {
    Integer id;
    String name;
}
