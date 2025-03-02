package sml.instructions;

import sml.*;

/**
 * Represents an addition instruction in the SML runtime environment.
 * *
 * <p>
 * This instruction pops two operands from the stack, performs addition, and pushes the
 * computed sum back onto the stack, implementing a stack-based addition.
 * </p>
 * <p>
 * If the stack contains fewer than two elements, an error will occur, halting execution.
 * Ensure sufficient operands are present before executing this instruction.
 * </p>
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 * </p>
 *
 * @author Ricki Angel
 */
public class AddInstruction extends Instruction {
    public static final String OP_CODE = "add";

    /**
     * Constructs an {@code AddInstruction} with the given label.
     *
     * @param label the label associated with this instruction
     */
    public AddInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the addition operation.
     * <p>
     * Pops the top two values from the stack, adds them, and pushes the result back onto the stack.
     * </p>
     *
     * @param frame the current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        frame.push(
                frame.pop() + frame.pop()
        );
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
