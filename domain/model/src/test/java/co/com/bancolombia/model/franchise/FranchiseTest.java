package co.com.bancolombia.model.franchise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {

    @Test
    void shouldBuildFranchiseWithCorrectValues() {
        Integer id = 1;
        String name = "Franchise One";

        Franchise franchise = Franchise.builder()
                .id(id)
                .name(name)
                .build();

        assertEquals(id, franchise.getId());
        assertEquals(name, franchise.getName());
    }

    @Test
    void shouldCreateModifiedCopyWithToBuilder() {
        Franchise originalFranchise = Franchise.builder()
                .id(1)
                .name("Franchise One")
                .build();

        Franchise modifiedFranchise = originalFranchise.toBuilder()
                .name("Modified Franchise")
                .build();

        assertEquals(1, modifiedFranchise.getId());
        assertEquals("Modified Franchise", modifiedFranchise.getName());
        assertNotEquals(originalFranchise, modifiedFranchise);
    }

    @Test
    void shouldMaintainEqualityForSameProperties() {
        Franchise franchise1 = Franchise.builder()
                .id(1)
                .name("Franchise One")
                .build();

        Franchise franchise2 = Franchise.builder()
                .id(1)
                .name("Franchise One")
                .build();

        assertEquals(franchise1, franchise2);
        assertEquals(franchise1.hashCode(), franchise2.hashCode());
    }
}