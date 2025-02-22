package sml.instruction;

import sml.*;
import java.util.Optional;
/**
 * ================================================================
 * Subtraction instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Removes the top two values from the stack, subtracts the most
 * recently added value from the second value, and places the
 * result back onto the stack.
 *
 * Follows the Last-In-First-Out (LIFO) stack manipulation principle.
 *
 * @author Ricki Angel
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

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(value1 - value2);
        return Optional.of(frame.advance());
    }
    @Override
    protected String getOperandsString() {
        return "";
    }
}