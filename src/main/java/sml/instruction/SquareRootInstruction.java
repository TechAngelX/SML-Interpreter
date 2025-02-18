package sml.instruction;

import sml.*;
import java.util.Optional;

/**
 * My Test class, to test injecting new class into
 * Square root instruction for SML.  Executes the square root operation.
 * Pops one number from the stack,
 * computes its square root, and pushes the result back.
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
        Frame frame = machine.frame();  // Get  current frame from the machine.
        int value = frame.pop();  // Pop onto stack.
        double result = Math.sqrt(value);  // Compute  square root.
        frame.push((int) result);  // Push result back to the stack (cast as int).
        return Optional.of(frame.advance());  // Return the updated frame.
    }


    /**
     * Returns a string version of the instruction's operands.
     * In this case, no operands are needed for the square root operation.
     *
     * @return the string representing the operands (empty string)
     */
    @Override
    protected String getOperandsString() {
        return "";
    }

}
