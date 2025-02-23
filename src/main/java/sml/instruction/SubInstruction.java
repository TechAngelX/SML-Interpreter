package sml.instruction;

import sml.*;
/**
 * =============================================================================
 * Subtraction instruction for Simple Machine Language (SML).
 * -----------------------------------------------------------------------------
 *
 * Removes the top two values from the stack, subtracts the most recently added
 * value from the second value, and places the result back onto the stack.
 *
 * Follows the Last-In-First-Out (LIFO) stack manipulation principle.
 *
 * @author Ricki Angel
 */
public class SubInstruction extends Instruction {
    public static final String OP_CODE = "sub";

    public SubInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(value1 - value2);
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
