package co.com.bancolombia.model.branch;


import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Branch {
     Integer id;
     String name;
     Integer franchiseId;
}
