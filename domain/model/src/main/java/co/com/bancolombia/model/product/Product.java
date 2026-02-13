package co.com.bancolombia.model.product;


import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Product {
     Integer id;
     String name;
     Integer stock;
     Integer branchId;
}
