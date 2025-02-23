package sml.instruction;

import sml.*;
/**
 * ================================================================
 * Division instruction for Simple Machine Language (SML).
 * ================================================================
 * <p>
 * Performs division of two operands from the stack, pushing the quotient
 * back onto the stack. Specifically, it pops the top two values, divides the
 * second-to-top value by the top value, and stores the result.
 * <p>
 * Handles division by zero conditions by throwing an
 * {@link ArithmeticException}.
 * <p>
 * * The {@code doExecute} method defines the instruction's core operational logic,
 *
 * @author Ricki Angel
 */
public class DivInstruction extends Instruction {
    public static final String OP_CODE = "div";

    public DivInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        if (value2 == 0) {
            throw new ArithmeticException("Division by zero");
        }
        frame.push(value1 / value2);
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
