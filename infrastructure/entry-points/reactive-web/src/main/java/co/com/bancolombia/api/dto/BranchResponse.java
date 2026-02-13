package co.com.bancolombia.api.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BranchResponse {
    Integer id;
    String name;
    Integer franchiseId;
}
