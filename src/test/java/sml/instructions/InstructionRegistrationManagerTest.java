package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import sml.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the InstructionRegistrationManager.
 *
 * <p>Validates the dynamic instruction discovery and registration capabilities 
 * of the Simple Machine Language instruction system.</p>
 *
 * <p>Key test objectives:</p>
 * <ul>
 *   <li>Verifying instruction creation for standard and coursework supplementary instructions</li>
 *   <li>Handling instructions with different parameter requirements</li>
 *   <li>Testing instruction creation with various label configurations</li>
 *   <li>Ensuring robust handling of unknown or invalid opcodes</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class InstructionRegistrationManagerTest {
    private Label label;

    @BeforeEach
    void setUp() {
        label = new Label("L1");
    }
    /**
     * Provides a stream of arguments for testing simple instructions that only require a Label.
     *
     * @return a stream of opcodes for parameterized testing
     */
    static Stream<Arguments> provideSimpleInstructionsData() {
        return Stream.of(
                // Instructions that only need a Label parameter:
                Arguments.of("add", AddInstruction.class),
                Arguments.of("sub", SubInstruction.class),
                Arguments.of("mul", MulInstruction.class),
                Arguments.of("div", DivInstruction.class),
                Arguments.of("print", PrintInstruction.class),
                Arguments.of("return", ReturnInstruction.class),
                Arguments.of("sqrt", SqrtInstruction.class),
                Arguments.of("not_eq", NotEqInstruction.class),
                Arguments.of("mod", ModInstruction.class)
        );
    }

    @ParameterizedTest
    @DisplayName("Simple instructions with just Label parameter should be created successfully")
    @MethodSource("provideSimpleInstructionsData")
    void testSimpleInstructionsCreation(String opcode, Class<? extends Instruction> expectedClass) {
        Instruction instruction = InstructionRegistrationManager.createInstruction(opcode, label);

        assertNotNull(instruction, "Failed to create instruction for opcode: " + opcode);
        assertEquals(opcode, instruction.opcode(), "Created instruction has incorrect opcode");
        assertTrue(expectedClass.isInstance(instruction),
                "Instruction for opcode '" + opcode + "' should be instance of " + expectedClass.getSimpleName());
    }

    /**
     * Acknowledges complex instructions that require additional parameters beyond a simple label.
     *
     * <p>This method serves as a documentation point for instructions with more complex 
     * instantiation requirements. It highlights instructions that need differing params such as:</p>
     * <ul>
     *   <li>Variable identifiers</li>
     *   <li>Target labels</li>
     *   <li>Method identifiers</li>
     * </ul>
     *
     * <p>Key points:</p>
     * <ul>
     *   <li>Not intended for direct instruction creation testing</li>
     *   <li>Documents the existence of complex instructions in the factory</li>
     *   <li>Indirect validation through dedicated integration and unit tests</li>
     * </ul>
     *
     * @param opcode The operation code for complex instructions requiring special handling
     */

    @ParameterizedTest
    @DisplayName("Instructions that need additional parameters should not be testable with just a label")
    @ValueSource(strings = {"load", "store", "push", "goto", "if_cmpeq", "if_cmpgt", "invoke"})
    void testComplexInstructions(String opcode) {
    
    }

    @Test
    @DisplayName("Returns null when attempting to create instruction with invalid opcode")
    void testCreateInstructionWithUnknownOpcode() {
        Instruction instruction = InstructionRegistrationManager.createInstruction("unknownOpcode", label);
        assertNull(instruction, "Instruction should be null for unknown opcode");
    }

    @Test
    @DisplayName("Created instructions should preserve their labels")
    void testInstructionLabelsArePreserved() {
        Label customLabel = new Label("CUSTOM_LABEL");
        Instruction instruction = InstructionRegistrationManager.createInstruction("add", customLabel);

        assertTrue(instruction.optionalLabel().isPresent(), "Label should be present");
        assertEquals(customLabel, instruction.optionalLabel().get(), "Label should match the provided label");
    }

    @Test
    @DisplayName("Can create instruction with null label")
    void testCreateInstructionWithNullLabel() {
        Instruction instruction = InstructionRegistrationManager.createInstruction("add", null);

        assertNotNull(instruction, "Should create instruction even with null label");
        assertFalse(instruction.optionalLabel().isPresent(), "Label should not be present");
    }

    @Test
    @DisplayName("Supplementary instructions should be created correctly")
    void testSupplementaryInstructions() {
        Instruction modInstruction = InstructionRegistrationManager.createInstruction("mod", label);
        assertNotNull(modInstruction, "ModInstruction was not registered properly");
        assertTrue(modInstruction instanceof ModInstruction, "Should create a ModInstruction for mod opcode");

        Instruction notEqInstruction = InstructionRegistrationManager.createInstruction("not_eq", label);
        assertNotNull(notEqInstruction, "NotEqInstruction was not registered properly");
        assertTrue(notEqInstruction instanceof NotEqInstruction, "Should create a NotEqInstruction for not_eq opcode");

        Instruction sqrtInstruction = InstructionRegistrationManager.createInstruction("sqrt", label);
        assertNotNull(sqrtInstruction, "SqrtInstruction was not registered properly");
        assertTrue(sqrtInstruction instanceof SqrtInstruction, "Should create a SqrtInstruction for sqrt opcode");
        
        // ... I can add more supplementary test instructions here if ness.
    }
}
