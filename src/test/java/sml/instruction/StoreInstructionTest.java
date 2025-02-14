package java.sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;
import sml.instruction.LoadInstruction;
import sml.instruction.ReturnInstruction;
import sml.instruction.StoreInstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that a value can be successfully stored in a variable using StoreInstruction,
 * verifying the complete store operation from stack to variable.
 */

public class StoreInstructionTest {
    private Machine machine;


    // Initialise frame BEFORE each test - otherwiase machine fram would be empty, causing NullPointerException.

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        machine = null;
    }

    /**
     * Tests that a value can be successfully stored in a variable.
     */
    @Test
    void executeValidStore() {
        // Create test variable identifier.
        Variable.Identifier varId = new Variable.Identifier("testVar");

        // Create store instruction and return instruction.
        Instruction storeInstruction = new StoreInstruction(null, varId);
        Instruction returnInstruction = new ReturnInstruction(null);

        // Create method with the instructions.
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(storeInstruction, returnInstruction)
        );

        // Set up the machine and push a test value onto the stack.
        machine.setProgram(List.of(mainMethod));

        // Push a test value onto the stack.
        machine.frame().push(42);

        storeInstruction.execute(machine);

        // Verify the value was stored correctly
        Variable var = machine.frame().variable(varId);
        assertEquals(42, var.load());
    }

    /**
     * Tests that attempting to store to a non-existent variable throws an exception.
     */
    @Test
    void executeWithNonExistentVariableShouldThrowException() {
        Variable.Identifier nonExistentVarId = new Variable.Identifier("nonExistentVar");

        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(new ReturnInstruction(null))  // Just a return instruction.
        );

        machine.setProgram(List.of(mainMethod));
        assertThrows(VariableNotFoundException.class,
                () -> machine.frame().variable(nonExistentVarId));
    }
    /**
     * Verifies that the variables() method returns a stream containing the single variable identifier.
     */
    @Test
    void storeInstructionVariablesShouldReturnSingleVariable() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        StoreInstruction storeInstruction = new StoreInstruction(null, varId);

        List<Variable.Identifier> variables = storeInstruction.variables()
                .toList();

        assertEquals(1, variables.size());
        assertEquals(varId, variables.get(0));
    }

    /**
     * Tests equals() method for StoreInstruction.
     */
    @Test
    void testEqualsMethod() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        Label label = new Label("testLabel");

        StoreInstruction instruction1 = new StoreInstruction(label, varId);
        StoreInstruction instruction2 = new StoreInstruction(label, varId);
        StoreInstruction instruction3 = new StoreInstruction(null, varId);

        // Test equality / inequality with identical instructions / dfferent labels.
        assertEquals(instruction1, instruction2);
        assertNotEquals(instruction1, instruction3);

        assertNotEquals(instruction1, null);
        Assertions.assertNotEquals(instruction1, new LoadInstruction(label, varId));
    }

    /**
     * Tests hashCode() method for StoreInstruction.
     */
    @Test
    void testHashCodeMethod() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        Label label = new Label("testLabel");

        StoreInstruction instruction1 = new StoreInstruction(label, varId);
        StoreInstruction instruction2 = new StoreInstruction(label, varId);

        // Test that equal objects have equal hash codes
        assertEquals(instruction1.hashCode(), instruction2.hashCode());
    }
}