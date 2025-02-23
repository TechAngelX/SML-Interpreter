package sml.instruction;

import sml.*;
/**
 * =====================================================================================
 * Multiplication instruction for Simple Machine Language (SML).
 * -------------------------------------------------------------------------------------
 * <p>
 * Takes two numbers from stack, multiplies them together,
 * and pushes the result back onto the stack.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */
public class MultiplyInstruction extends Instruction {
    public static final String OP_CODE = "mul";

    public MultiplyInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(value1 * value2);
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}

