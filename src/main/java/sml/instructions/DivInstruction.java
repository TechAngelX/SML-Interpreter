package sml.instructions;

import sml.*;

/**
 * Represents a Division instruction in the SML runtime environment.
 * *
 * <p>
 * This instruction performs integer division using values from the stack. It pops the
 * top two values, divides the second-to-top value by the top value, and pushes the quotient
 * back onto the stack.
 * </p>
 * <p>
 * If division by zero is attempted, an {@link ArithmeticException} is thrown.
 * </p>
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 * </p>
 *
 * @author Ricki Angel
 */
public non-sealed class DivInstruction extends Instruction {
    public static final String OP_CODE = "div";

    /**
     * Constructs a {@code DivInstruction} with the given label.
     *
     * @param label the label associated with this instruction
     */
    public DivInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the division operation.
     * <p>
     * Pops the top two values from the stack, divides the second value by the first, and
     * pushes the result back onto the stack.
     * </p>
     * <p>
     * Throws an {@link ArithmeticException} if division by zero is attempted.
     * </p>
     *
     * @param frame the current execution frame
     * @throws ArithmeticException if division by zero occurs
     */
    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        if (value2 == 0) {
            throw new ArithmeticException("Division by zero");
        }
        frame.push(value1 / value2);
    }

    /**
     * Returns a string representation of the operands.
     *
     * @return an empty string since this instruction does not take explicit operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
