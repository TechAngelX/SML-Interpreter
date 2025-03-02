package sml.instructions;

import sml.*;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the StoreInstruction in the Simple Machine Language.
 *
 * <p>Validates the behavior of variable storage instructions within the SML runtime.</p>
 *
 * <p>Key test objectives:</p>
 * <ul>
 *   <li>Storing values from the stack to variables</li>
 *   <li>Handling non-existent variable scenarios</li>
 *   <li>Verifying variable storage method implementation</li>
 * </ul>
 *
 * @author Ricki Angel
 */

public class StoreInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        machine = null;
    }

    @Test
    @DisplayName("Should successfully store a value from stack to a variable")
    void testStoreValueFromStackToVariable() {
        Variable.Identifier varId = new Variable.Identifier("testVar");

        Instruction storeInstruction = new StoreInstruction(null, varId);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(storeInstruction, returnInstruction)
        );
        machine.setProgram(List.of(mainMethod));
        machine.frame().push(42);
        storeInstruction.execute(machine);

        Variable var = machine.frame().variable(varId);
        assertEquals(42, var.load());
    }

    @Test
    @DisplayName("Should throw VariableNotFoundException for non-existent variable")
    void testNonExistentVariableShouldThrowException() {
        Variable.Identifier nonExistentVarId = new Variable.Identifier("nonExistentVar");

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(new ReturnInstruction(null))
        );
        machine.setProgram(List.of(mainMethod));
        assertThrows(VariableNotFoundException.class,
                () -> machine.frame().variable(nonExistentVarId));
    }

    @Test
    @DisplayName("Should return the correct variable when calling variables() method")
    void testStoreInstructionVariablesShouldReturnSingleVariable() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        StoreInstruction storeInstruction = new StoreInstruction(null, varId);

        List<Variable.Identifier> variables = storeInstruction.variables()
                .toList();

        assertEquals(1, variables.size());
        assertEquals(varId, variables.get(0));
    }

    @Test
    @DisplayName("Should correctly implement equals() method")
    void testEqualsMethod() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        Label label = new Label("testLabel");

        StoreInstruction instruction1 = new StoreInstruction(label, varId);
        StoreInstruction instruction2 = new StoreInstruction(label, varId);
        StoreInstruction instruction3 = new StoreInstruction(null, varId);

        assertEquals(instruction1, instruction2);
        assertNotEquals(instruction1, instruction3);

        assertNotEquals(instruction1, null);
        Assertions.assertNotEquals(instruction1, new LoadInstruction(label, varId));
    }

    @Test
    @DisplayName("Should generate consistent hash codes for equal objects")
    void testHashCodeMethod() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        Label label = new Label("testLabel");

        StoreInstruction instruction1 = new StoreInstruction(label, varId);
        StoreInstruction instruction2 = new StoreInstruction(label, varId);

        assertEquals(instruction1.hashCode(), instruction2.hashCode());
    }
}
