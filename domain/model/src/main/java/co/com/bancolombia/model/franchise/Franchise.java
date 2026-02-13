package co.com.bancolombia.model.franchise;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Franchise {
     Integer id;
     String name;
}