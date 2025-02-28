package sml.instructions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sml.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the InstructionRegistrationManager class.
 *
 * These tests verify the discovery and creation capabilities of the instruction 
 * registration system, ensuring that instructions are properly registered and instantiated.
 *
 * @author Ricki Angel
 */
class InstructionRegistrationManagerTest {
    private Label label;

    @BeforeEach
    void setUp() {
        label = new Label("L1");
    }

    @ParameterizedTest
    @DisplayName("Simple instructions with just Label parameter should be created successfully")
    @ValueSource(strings = {"add", "sub", "mul", "div", "print", "return"})
    void testSimpleInstructionsCreation(String opcode) {
        Instruction instruction = InstructionRegistrationManager.createInstruction(opcode, label);

        assertNotNull(instruction, "Failed to create instruction for opcode: " + opcode);
        assertEquals(opcode, instruction.opcode());
    }

    @ParameterizedTest
    @DisplayName("Instructions requiring additional parameters should be created successfully")
    @ValueSource(strings = {"not_eq", "mod"})
    void testSpecialInstructions(String opcode) {
        Instruction instruction = InstructionRegistrationManager.createInstruction(opcode, label);

        assertNotNull(instruction, "Failed to create instruction for opcode: " + opcode);
        assertEquals(opcode, instruction.opcode());
    }
    
    @Test
    @DisplayName("Should create ModInstruction for mod opcode")
    void testCreateModInstruction() {
        Instruction instruction = InstructionRegistrationManager.createInstruction("mod", label);

        assertNotNull(instruction, "ModInstruction was not registered properly");
        assertTrue(instruction instanceof ModInstruction,
                "Should create a ModInstruction for mod opcode");
        assertEquals("mod", instruction.opcode(), "Created instruction has incorrect opcode");
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
}
