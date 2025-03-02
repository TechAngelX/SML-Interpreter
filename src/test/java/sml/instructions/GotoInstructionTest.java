package sml.instructions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the GotoInstruction in the Simple Machine Language.
 *
 * <p>Validates the functionality of unconditional jump instructions within the SML runtime.</p>
 *
 * <p>Key test objectives:</p>
 * <ul>
 *   <li>Verifying successful label jumping</li>
 *   <li>Handling error scenarios for missing labels</li>
 *   <li>Ensuring correct program counter manipulation</li>
 * </ul>
 *
 * @author Ricki Angel
 */

public class GotoInstructionTest {
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
    @DisplayName("Should successfully execute a valid goto instruction")
    void validGotoInstruction() {
        Instruction ins0 = new GotoInstruction(null, new Label("L1"));
        Instruction ins1 = new ReturnInstruction(null);
        Instruction ins2 = new ReturnInstruction(new Label("L1"));

        Method m = new Method(new Method.Identifier("@main"), List.of(), List.of(ins0, ins1, ins2));
        machine.setProgram(List.of(m));
        Optional<Frame> frame = ins0.execute(machine);
        assertEquals(2, frame.get().programCounter());
    }

    @Test
    @DisplayName("Should throw LabelNotFoundException for missing labels")
    void missingLabelGotoInstruction() {
        Label missingLabel = new Label("L2");

        Instruction ins0 = new GotoInstruction(null, missingLabel);
        Instruction ins1 = new ReturnInstruction(null);

        Method m = new Method(new Method.Identifier("@main"), List.of(), List.of(ins0, ins1));

        machine.setProgram(List.of(m));

        // Execute the GotoInstruction (ins0), expecting it to throw a LabelNotFoundException
        // because the label "L2" is missing:
        LabelNotFoundException ex = assertThrows(LabelNotFoundException.class, () -> ins0.execute(machine));


        assertEquals("Label L2 not found in main", ex.getMessage());

        assertEquals(missingLabel, ex.getLabel(), "Exception should contain the missing label");
        assertEquals(m, ex.getMethod(), "Exception should reference the correct method");
    }
}
