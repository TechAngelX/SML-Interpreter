package sml.instructions;

import sml.*;

/**
 * =====================================================================================
 * Return instruction for Simple Machine Language (SML).
 * -------------------------------------------------------------------------------------
 * <p>
 * Handles method return mechanism by popping the top value
 * from the current frame and pushing it to the invoking frame.
 * <p>
 * Manages method call stack and value propagation between method contexts.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */
public non-sealed class ReturnInstruction extends Instruction {
    public static final String OP_CODE = "return";

    public ReturnInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value = frame.pop();
        frame.invoker().ifPresent(invoker -> invoker.push(value));
    }

    @Override
    protected Frame determineNextFrame(Frame frame) {
        return frame.invoker()
                .map(Frame::advance)
                .orElse(null);
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
