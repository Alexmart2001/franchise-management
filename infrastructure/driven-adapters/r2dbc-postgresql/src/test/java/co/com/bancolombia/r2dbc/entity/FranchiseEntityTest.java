package co.com.bancolombia.r2dbc.entity;

import co.com.bancolombia.model.franchise.Franchise;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseEntityTest {

    @Test
    void shouldConvertFromDomainToEntity() {
        Franchise franchise = Franchise.builder()
                .id(1)
                .name("Franchise Name")
                .build();

        FranchiseEntity franchiseEntity = FranchiseEntity.fromDomain(franchise);

        assertThat(franchiseEntity).isNotNull();
        assertThat(franchiseEntity.getId()).isEqualTo(franchise.getId());
        assertThat(franchiseEntity.getName()).isEqualTo(franchise.getName());
    }

    @Test
    void shouldConvertEntityToDomain() {
        FranchiseEntity franchiseEntity = FranchiseEntity.builder()
                .id(1)
                .name("Franchise Name")
                .build();

        Franchise franchise = franchiseEntity.toDomain();

        assertThat(franchise).isNotNull();
        assertThat(franchise.getId()).isEqualTo(franchiseEntity.getId());
        assertThat(franchise.getName()).isEqualTo(franchiseEntity.getName());
    }

    @Test
    void shouldBuildEntityProperly() {
        FranchiseEntity franchiseEntity = FranchiseEntity.builder()
                .id(1)
                .name("Test Franchise")
                .build();

        assertThat(franchiseEntity).isNotNull();
        assertThat(franchiseEntity.getId()).isEqualTo(1);
        assertThat(franchiseEntity.getName()).isEqualTo("Test Franchise");
    }

    @Test
    void shouldBuildDomainProperly() {
        Franchise franchise = Franchise.builder()
                .id(1)
                .name("Test Franchise")
                .build();

        assertThat(franchise).isNotNull();
        assertThat(franchise.getId()).isEqualTo(1);
        assertThat(franchise.getName()).isEqualTo("Test Franchise");
    }
}