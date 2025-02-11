package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO retrive a value and push onto current stack
public class LoadInstructionTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream(); // Capture output.
    private Machine machine;

    @BeforeEach
    void setUp() { // Create a new instance of the machine before each test.
        machine = new Machine();
    }

    @AfterEach
    void tearDown() { // Clean up the machine instance after each test.
        machine = null;
    }

    @Test
    void testLoadInstructionPushesAndPrintsValue() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        Instruction pushValInstruction0 = new LoadInstruction(null, varId); // Create LoadInstruction instance that loads from variable "testVar".
        Instruction returnInstruction = new ReturnInstruction(null);  // End execution.

        Method m = new Method(new Method.Identifier("@main"),
                List.of(),
                List.of(pushValInstruction0, returnInstruction));
        machine.setProgram(List.of(m));

    // Use the 'localVariables()' method from Method.
        Variable var = machine.frame().variable(varId);
        var.store(100);

        System.setOut(new PrintStream(outContent));
        pushValInstruction0.execute(machine);

        assertEquals("100\n", outContent.toString());
    }
}