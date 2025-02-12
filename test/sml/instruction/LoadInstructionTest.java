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
        System.setOut(System.out);  // Reset System.out to its original state
    }

    //Tests the main functionality of LoadInstruction
    //Checks if it loads a value (55) and prints it correctly
    @Test
    void testLoadInstructionPushesAndPrintsValue() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        Instruction pushValInstruction0 = new LoadInstruction(null, varId); // Create LoadInstruction instance that loads from variable "testVar".
        Instruction returnInstruction = new ReturnInstruction(null);  // End

        Method m = new Method(new Method.Identifier("@main"),
                List.of(),
                List.of(pushValInstruction0, returnInstruction));
        machine.setProgram(List.of(m));

        // Use the 'localVariables()' method from Method.
        Variable var = machine.frame().variable(varId);
        var.store(55);

        System.setOut(new PrintStream(outContent));
        pushValInstruction0.execute(machine);

        assertEquals("55\n", outContent.toString());
    }


    // Tests the variables() method inherited from AbstractVarInstruction
    // Checks if it correctly returns a stream containing just the single variable identifier
    @Test
    void testVariablesReturnsCorrectVariable() {
        // Test the variables() method that was moved to AbstractVarInstruction
        Variable.Identifier varId = new Variable.Identifier("testVar");
        LoadInstruction instruction = new LoadInstruction(null, varId);
        List<Variable.Identifier> vars = instruction.variables().collect(Collectors.toList());
        assertEquals(1, vars.size());
        assertEquals(varId, vars.get(0));
    }
}