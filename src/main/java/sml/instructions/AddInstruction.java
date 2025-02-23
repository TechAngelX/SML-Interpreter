package sml.instructions;

import sml.*;
/**
 * ==============================================================================
 * Add instruction for Simple Machine Language (SML).
 * ------------------------------------------------------------------------------
 * <p>
 * Pops two operands from the stack, performs addition, and pushes the
 * computed sum back onto the stack. Essentially, a classic stack-based addition.
 * <p>
 * If the stack contains fewer than two elements, this operation
 * will trigger an error, halting execution. Ensure sufficient operands are
 * present before executing this instruction.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic,
 *
 * @author Ricki Angel
 */
public non-sealed class AddInstruction extends Instruction {
    public static final String OP_CODE = "add";

    public AddInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(value1 + value2);
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}