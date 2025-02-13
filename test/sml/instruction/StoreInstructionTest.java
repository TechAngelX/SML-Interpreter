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
// This test verifies the basic functionality of the PushInstruction by ensuring it can:
// - Push a specified value (e.g., 55) onto the machine's operand stack
// - Correctly add the value to the stack, which is then confirmed by retrieving (popping) it

    @Test
    void testStoreInstructionStoresValueInVariable() {
        Variable.Identifier sourceVarId = new Variable.Identifier("sourceVar");
        Variable sourceVar = machine.frame().variable(sourceVarId);
        sourceVar.store(55);

        // Create instructions to load from source and store to target
        Instruction storeValInstruction = new StoreInstruction(null, sourceVarId);
        Variable.Identifier targetVarId = new Variable.Identifier("targetVar");
        Instruction storeInstruction = new StoreInstruction(null, targetVarId);

        // Create method with instructions
        Method m = new Method(new Method.Identifier("@main"),
                List.of(),
                List.of(storeInstruction, storeInstruction));

        machine.setProgram(List.of(m));
        machine.execute();

        // Check if the value is correctly stored in the target variable
        Variable targetVar = machine.frame().variable(targetVarId);
        assertEquals(55, targetVar.load());

        // Check the printed output (due to LoadInstruction's print)
        assertEquals("55\n", outContent.toString());
    }


}