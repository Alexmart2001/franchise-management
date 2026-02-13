package co.com.bancolombia.model.product;


import lombok.Data;

@Data
public class Product {
    private Integer id;
    private String name;
    private Integer stock;
    private Integer branchId;
}
