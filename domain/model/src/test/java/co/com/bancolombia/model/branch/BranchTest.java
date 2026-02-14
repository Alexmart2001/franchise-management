package co.com.bancolombia.model.branch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BranchTest {

    @Test
    void testBranchBuilder() {
        Branch branch = Branch.builder()
                .id(1)
                .name("Main Office")
                .franchiseId(10)
                .build();

        assertEquals(1, branch.getId());
        assertEquals("Main Office", branch.getName());
        assertEquals(10, branch.getFranchiseId());
    }

    @Test
    void testBranchImmutability() {
        Branch branch = Branch.builder()
                .id(1)
                .name("Main Office")
                .franchiseId(10)
                .build();

        Branch updatedBranch = branch.toBuilder()
                .name("New Office")
                .build();

        assertEquals("New Office", updatedBranch.getName());

        assertEquals("Main Office", branch.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        Branch branch1 = Branch.builder()
                .id(1)
                .name("Main Office")
                .franchiseId(10)
                .build();

        Branch branch2 = Branch.builder()
                .id(1)
                .name("Main Office")
                .franchiseId(10)
                .build();

        assertEquals(branch1, branch2);

        assertEquals(branch1.hashCode(), branch2.hashCode());
    }

    @Test
    void testToString() {
        Branch branch = Branch.builder()
                .id(1)
                .name("Main Office")
                .franchiseId(10)
                .build();

        String expectedString = "Branch(id=1, name=Main Office, franchiseId=10)";
        assertEquals(expectedString, branch.toString());
    }
}