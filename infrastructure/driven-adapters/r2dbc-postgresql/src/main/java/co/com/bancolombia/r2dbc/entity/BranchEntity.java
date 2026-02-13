package co.com.bancolombia.r2dbc.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import co.com.bancolombia.model.branch.Branch;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("branch")
public class BranchEntity {
    @Id
    private Integer id;
    private String name;
    private Integer franchiseId;

    public static BranchEntity fromDomain(Branch branch) {
        return BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }

    public Branch toDomain() {
        return Branch.builder()
                .id(this.id)
                .name(this.name)
                .franchiseId(this.franchiseId)
                .build();
    }
}