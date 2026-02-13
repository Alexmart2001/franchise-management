package co.com.bancolombia.r2dbc.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import co.com.bancolombia.model.franchise.Franchise;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("franchise")
public class FranchiseEntity {
    @Id

    private Integer id;
    private String name;

    public static FranchiseEntity fromDomain(Franchise franchise) {
        return FranchiseEntity.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }

    public Franchise toDomain() {
        return Franchise.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}