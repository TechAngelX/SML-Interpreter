package sml.instructions;

import sml.*;

/**
 * =====================================================================================
 * Represents the 'push' instruction in the Simple Machine Language (SML).
 * -------------------------------------------------------------------------------------
 * Pushes a constant integer value directly onto the current frame's operand stack.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */
public class PushInstruction extends Instruction {
    public static final String OP_CODE = "push";
    private final int value;

    public PushInstruction(Label label, int value) {
        super(label, OP_CODE);
        this.value = value;
    }

    @Override
    protected void doExecute(Frame frame) {
        frame.push(value);
    }

    @Override
    protected String getOperandsString() {
        return String.valueOf(value);
    }
}