package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreInstructionTest {
    private Machine machine;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        machine = new Machine();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        machine = null;
        System.setOut(System.out);
    }

    // This test verifies the basic functionality of the StoreInstruction by ensuring it can:
    // - Take a value from the operand stack
    // - Store that value in a specified variable
    // - Ensure the value is correctly saved in the variable
    @Test
    void testStoreInstructionStoresValueInVariable() {
        Variable.Identifier varId = new Variable.Identifier("testStoreVar");
        // Store a value onto the stack
        StoreInstruction storeInstruction = new StoreInstruction(null,varId);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method m = new Method(new Method.Identifier("@main"),

        List.of(), List.of(storeInstruction, returnInstruction));
        machine.setProgram(List.of(m));

        // Check if the value is correctly stored in the target variable
        Variable targetVar = machine.frame().variable(varId); // Fetch the variable from the machine's frame
        assertEquals(55, targetVar.load()); // Verify that the stored value is 55
    }

    /**
     * Verifies that the variables() method for LoadInstruction returns a stream containing the single variable identifier.
     */
    @Test
    void storeInstructionVariablesShouldReturnSingleVariable() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        LoadInstruction loadInstruction = new LoadInstruction(null, varId);
        List<Variable.Identifier> variables = loadInstruction.variables().collect(Collectors.toList());
        assertEquals(1, variables.size());
        assertEquals(varId, variables.get(0));
    }

}
