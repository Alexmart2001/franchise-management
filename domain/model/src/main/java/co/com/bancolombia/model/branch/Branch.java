package co.com.bancolombia.model.branch;


import lombok.Data;

@Data
public class Branch {
    private Integer id;
    private String name;
    private Integer franchiseId;
}
