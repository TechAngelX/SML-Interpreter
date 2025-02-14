package sml.instruction;

import sml.*;
import java.util.Optional;
/**
 * Represents the 'sub' instruction in the Simple Machine Language (SML).
 * ======================================================================

 * Performs subtraction by removing the top two values from the stack,
 * subtracting the most recently added value from the second value,
 * and placing the result back onto the stack.
 * Follows the Last-In-First-Out (LIFO) stack manipulation principle.
 */
public class SubInstruction extends Instruction {
    public static final String OP_CODE = "sub";

    /**
     * Constructor for the Sub instruction class.
     * ==========================================
     *
     * @param label optional label (can be null)
     */
    public SubInstruction(Label label) {
        super(label, OP_CODE);
    }

    // ==================================== Methods ======================================

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value2 = frame.pop();  // Take top number
        int value1 = frame.pop();  // Take next number
        frame.push(value1 - value2); // Put result back
        return Optional.of(frame.advance());
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}