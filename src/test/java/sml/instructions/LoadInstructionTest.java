package sml.instructions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sml.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for validating the LoadInstruction's functionality in the Simple Machine Language.
 *
 * <p>Focuses on verifying critical behaviors of variable loading operations:</p>
 * <ul>
 *   <li>Correctly loading values from variables</li>
 *   <li>Pushing loaded values onto the operand stack</li>
 *   <li>Ensuring proper interaction with the machine's execution frame</li>
 * </ul>
 *
 * <p>Test scenarios cover essential load instruction behaviors to guarantee
 * reliable variable value retrieval and stack manipulation.</p>
 *
 * @author Ricki Angel
 */
public class LoadInstructionTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        machine = null;
        System.setOut(System.out);
    }

    @Test
    @DisplayName("Should load and print a value from a variable")
    void loadInstructionShouldLoadAndPrintValue() {
        Variable.Identifier varId = new Variable.Identifier("testLoadVar");
        Instruction loadInstruction = new LoadInstruction(null, varId);
        Instruction returnInstruction = new ReturnInstruction(null);

        Method mainMethod = new Method(new Method.Identifier("@main"),
                List.of(), List.of(loadInstruction, returnInstruction));
        machine.setProgram(List.of(mainMethod));

        Variable variable = machine.frame().variable(varId);
        variable.store(55);

        System.setOut(new PrintStream(outContent));
        loadInstruction.execute(machine);

        assertEquals("55\n", outContent.toString());
    }

    @Test
    @DisplayName("Should return the correct variable when calling variables() method")
    void loadInstructionVariablesShouldReturnSingleVariable() {
        Variable.Identifier varId = new Variable.Identifier("testVar");
        LoadInstruction loadInstruction = new LoadInstruction(null, varId);
        List<Variable.Identifier> variables = loadInstruction.variables().collect(Collectors.toList());
        assertEquals(1, variables.size());
        assertEquals(varId, variables.get(0));
    }
}
