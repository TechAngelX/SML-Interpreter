package sml.instruction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubInstructionTest {
    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
        Method mainMethod = new Method(
                new Method.Identifier("@main"),
                List.of(),
                List.of(new ReturnInstruction(null))
        );
        machine.setProgram(List.of(mainMethod));
    }

    @Test
    void executeSubInstruction() {
        machine.frame().push(34);  // Note the First number.
        machine.frame().push(16);  // And the Second number.

        Instruction instruction = new SubInstruction(null);
        instruction.execute(machine);

        int result = machine.frame().pop();
        assertEquals(18, result, "34 - 16 should equal 18");
    }
}