package sml.instructions;

import sml.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionFactoryTest {
    private Label label;

    @BeforeEach
    void setUp() {
        label = new Label("L1");
    }

    @Test
    @DisplayName("Should create NotEqInstruction for not_eq opcode")
    void testCreateNotEqInstruction() {
        Instruction instruction = InstructionFactory.createInstruction("not_eq", label);  // Change this opcode for to test a a new Instruction.

        assertNotNull(instruction, "Instruction class not in /instructions folder");
        assertTrue(instruction instanceof NotEqInstruction, // Change this classname for your test.
                "Should create an NotEqInstruction for not_eq opcode");
    }

    @Test
    @DisplayName("Creates an unconditional jump instruction from L1 to target label (location) 'L4'")
    void testCreateGotoInstruction() {
        Label targetLabel = new Label("L4");
        Instruction instruction = InstructionFactory.createGotoInstruction(label, targetLabel);

        assertNotNull(instruction, "Instruction should not be null");
        assertTrue(instruction instanceof GotoInstruction, "Instruction should be an instance of GotoInstruction");
        GotoInstruction gotoInstruction = (GotoInstruction) instruction;
        assertEquals("L4", gotoInstruction.getOperandsString(), "Target label should be L4");
    }

    @Test
    @DisplayName("Creates a conditional jump instruction that jumps to 'L8' if first value > second value")
    void testCreateIfCmpgtInstruction() {
        Label targetLabel = new Label("L8");
        Instruction instruction = InstructionFactory.createIfCmpgtInstruction(label, targetLabel);

        assertNotNull(instruction, "Instruction should not be null");
        assertTrue(instruction instanceof IfCmpgtInstruction, "Instruction should be an instance of IfCmpgtInstruction");
        IfCmpgtInstruction ifCmpgtInstruction = (IfCmpgtInstruction) instruction;
        assertEquals("L8", ifCmpgtInstruction.getOperandsString(), "Target label should be L8");
    }

    @Test
    @DisplayName("Creates a load instruction that reads value from variable 'var1'")
    void testCreateLoadInstruction() {
        Variable.Identifier varId = new Variable.Identifier("var1");
        Instruction instruction = InstructionFactory.createLoadInstruction(label, varId);

        assertNotNull(instruction, "Instruction should not be null");
        assertTrue(instruction instanceof LoadInstruction, "Instruction should be an instance of LoadInstruction");
        LoadInstruction loadInstruction = (LoadInstruction) instruction;
        assertEquals("var1", loadInstruction.getOperandsString(), "Variable identifier should match");
    }

    @Test
    @DisplayName("Returns null when attempting to create instruction with invalid opcode")
    void testCreateInstructionWithUnknownOpcode() {
        Instruction instruction = InstructionFactory.createInstruction("unknownOpcode", label);
        assertNull(instruction, "Instruction should be null for unknown opcode");
    }

}
