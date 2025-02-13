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

/**
 * Unit tests for the {@link LoadInstruction} class.
 */
public class LoadInstructionTest {
    // An optional stream for capturing console output as a byte array, useful for test verification.
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Machine machine;

    @BeforeEach
    void setUp() {
        // Initialize a new Machine instance before each test.
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        // Clean up the Machine instance and reset System.out to its default.
        machine = null;
        System.setOut(System.out);
    }


    //Tests the main functionality of LoadInstruction,
    //Checks if it loads a value (55) and prints it correctly
    @Test
    void loadInstructionShouldLoadAndPrintValue() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        Instruction loadInstruction = new LoadInstruction(null, varId);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(new Method.Identifier("@main"),
                List.of(), List.of(loadInstruction, returnInstruction));
        machine.setProgram(List.of(mainMethod));

        // Set the value of the variable before executing the instruction.
        Variable variable = machine.frame().variable(varId);
        variable.store(55);

        // Redirect System.out to capture the output.
        System.setOut(new PrintStream(outContent));
        loadInstruction.execute(machine);

        assertEquals("55\n", outContent.toString());
    }

    /**
     * Verifies that the variables() method for LoadInstruction returns a stream containing the single variable identifier.
     */
    @Test
    void loadInstructionVariablesShouldReturnSingleVariable() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        LoadInstruction loadInstruction = new LoadInstruction(null, varId);
        List<Variable.Identifier> variables = loadInstruction.variables().collect(Collectors.toList());
        assertEquals(1, variables.size());
        assertEquals(varId, variables.get(0));
    }
}
