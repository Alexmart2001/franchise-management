package co.com.bancolombia.r2dbc.entity;

import co.com.bancolombia.model.branch.Branch;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchEntityTest {

    @Test
    void shouldConvertFromDomainToEntity() {
        Branch branch = Branch.builder()
                .id(1)
                .name("Branch Name")
                .franchiseId(100)
                .build();

        BranchEntity branchEntity = BranchEntity.fromDomain(branch);

        assertThat(branchEntity).isNotNull();
        assertThat(branchEntity.getId()).isEqualTo(branch.getId());
        assertThat(branchEntity.getName()).isEqualTo(branch.getName());
        assertThat(branchEntity.getFranchiseId()).isEqualTo(branch.getFranchiseId());
    }

    @Test
    void shouldConvertEntityToDomain() {
        BranchEntity branchEntity = BranchEntity.builder()
                .id(1)
                .name("Branch Name")
                .franchiseId(100)
                .build();

        Branch branch = branchEntity.toDomain();

        assertThat(branch).isNotNull();
        assertThat(branch.getId()).isEqualTo(branchEntity.getId());
        assertThat(branch.getName()).isEqualTo(branchEntity.getName());
        assertThat(branch.getFranchiseId()).isEqualTo(branchEntity.getFranchiseId());
    }

    @Test
    void shouldBuildEntityProperly() {
        BranchEntity branchEntity = BranchEntity.builder()
                .id(1)
                .name("Test Branch")
                .franchiseId(10)
                .build();

        assertThat(branchEntity).isNotNull();
        assertThat(branchEntity.getId()).isEqualTo(1);
        assertThat(branchEntity.getName()).isEqualTo("Test Branch");
        assertThat(branchEntity.getFranchiseId()).isEqualTo(10);
    }

    @Test
    void shouldBuildDomainProperly() {
        Branch branch = Branch.builder()
                .id(1)
                .name("Test Branch")
                .franchiseId(10)
                .build();

        assertThat(branch).isNotNull();
        assertThat(branch.getId()).isEqualTo(1);
        assertThat(branch.getName()).isEqualTo("Test Branch");
        assertThat(branch.getFranchiseId()).isEqualTo(10);
    }
}