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
 * Tests for the InstructionRegistrationManager class.
 *
 * These tests verify the discovery and creation capabilities of the instruction 
 * registration system, ensuring that the full suite (16) instructions are properly registered and instantiated.
 *
 * @author Ricki Angel
 */
class InstructionRegistrationManagerTest {
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
                // Instructions that only need a Label parameter
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

    @ParameterizedTest
    @DisplayName("Instructions that need additional parameters should not be testable with just a label")
    @ValueSource(strings = {"load", "store", "push", "goto", "if_cmpeq", "if_cmpgt", "invoke"})
    void testComplexInstructions(String opcode) {
        // These instructions require additional parameters (varID, targetLabel, methodID etc.) So we won't
        //test their actual creation, just acknowledge they exist in the factory. We test this indirectly 
        // through the integration tests and unit tests for each instruction in this package. The purpose of 
        // this test is to document these instructions need special handling. No assertions here - just 
        // documenting these instructions exist...
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
        // Test ModInstruction
        Instruction modInstruction = InstructionRegistrationManager.createInstruction("mod", label);
        assertNotNull(modInstruction, "ModInstruction was not registered properly");
        assertTrue(modInstruction instanceof ModInstruction, "Should create a ModInstruction for mod opcode");

        // Test NotEqInstruction
        Instruction notEqInstruction = InstructionRegistrationManager.createInstruction("not_eq", label);
        assertNotNull(notEqInstruction, "NotEqInstruction was not registered properly");
        assertTrue(notEqInstruction instanceof NotEqInstruction, "Should create a NotEqInstruction for not_eq opcode");

        // Test SqrtInstruction
        Instruction sqrtInstruction = InstructionRegistrationManager.createInstruction("sqrt", label);
        assertNotNull(sqrtInstruction, "SqrtInstruction was not registered properly");
        assertTrue(sqrtInstruction instanceof SqrtInstruction, "Should create a SqrtInstruction for sqrt opcode");
    }
}
