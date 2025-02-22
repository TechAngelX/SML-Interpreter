package sml.instruction;

import sml.*;
import java.util.Optional;

/**
 * ====================================================================================================================
 * A bespoke instruction file to test injecting new future instructions into the InstructionFactory.
 * --------------------------------------------------------------------------------------------------------------------
 * Provides a test case for dynamic instruction extension in the Simple Machine Language interpreter and
 * particularly the ServiceLoader function.
 * ====================================================================================================================
 * @author Ricki Angel
 */
public class SquareRootInstruction extends Instruction {
    public static final String OP_CODE = "sqrt";

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label  optional label (can be null)
     */
    public SquareRootInstruction(Label label) {
        super(label,OP_CODE);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value = frame.pop();
        double result = Math.sqrt(value);
        frame.push((int) result);
        return Optional.of(frame.advance());     }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
