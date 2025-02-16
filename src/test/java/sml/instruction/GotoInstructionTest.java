package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sml.*;
import sml.instruction.GotoInstruction;
import sml.instruction.ReturnInstruction;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class GotoInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @AfterEach
    
    void tearDown() {
        machine = null;
    }

    @Test
    void validGotoInstruction() {
        Instruction ins0 = new GotoInstruction(null, new Label("L1"));
        Instruction ins1 = new ReturnInstruction(null);
        Instruction ins2 = new ReturnInstruction(new Label("L1"));

        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1, ins2));
        machine.setProgram(List.of(m));
        Optional<Frame> frame = ins0.execute(machine);
        assertEquals(2, frame.get().programCounter());
    }


    /**
     * Tests that a {@link GotoInstruction} throws a {@link LabelNotFoundException} when the
     * target label is missing. The test checks that the exception message, missing label, and
     * method are correctly set when a label is not found in the method's program.
     *
     * @throws LabelNotFoundException if the referenced label is not found in the method.
     */

    @Test
    void missingLabelGotoInstruction() {
        // Create a Label instance representing the missing label "L2".
        Label missingLabel = new Label("L2");


        Instruction ins0 = new GotoInstruction(null, missingLabel);
        Instruction ins1 = new ReturnInstruction(null);

        // Create a Method instance named "@main" that contains the instructions ins0 (Goto)
        // and ins1 (Return).
        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));

        // Set the program.
        machine.setProgram(List.of(m));

        // Execute the GotoInstruction (ins0), expecting it to throw a LabelNotFoundException
        // because the label "L2" is missing.
        LabelNotFoundException ex = assertThrows(LabelNotFoundException.class, () -> ins0.execute(machine));

        // Verify the exception message to ensure it correctly indicates
        // that "Label L2 not found in main".
        assertEquals("Label L2 not found in main", ex.getMessage());

        assertEquals(missingLabel, ex.getLabel(), "Exception should contain the missing label");

        assertEquals(m, ex.getMethod(), "Exception should reference the correct method");
    }


}