package sml.instructions;

import sml.*;
/**
 * =====================================================================================
 * Conditional Jump (If Equal) instruction for Simple Machine Language (SML).
 * -------------------------------------------------------------------------------------
 * <p>
 * Performs a conditional jump based on the equality of two values from the stack.
 * Specifically, it pops two values, compares them, and if they are equal, transfers
 * control to the instruction associated with the specified label. Otherwise, execution
 * proceeds to the next sequential instruction.
 * <p>
 * This instruction enables conditional branching, allowing for dynamic program flow based
 * on runtime data.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic,
 *
 * @author Ricki Angel
 */

public class IfCmpeqInstruction extends Instruction {
    public static final String OP_CODE = "if_cmpeq";
    private final Label jumpLabel;
    private boolean shouldJump;

    public IfCmpeqInstruction(Label label, Label jumpLabel) {
        super(label, OP_CODE);
        this.jumpLabel = jumpLabel;
    }

    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        shouldJump = (value1 == value2);
    }

    @Override
    protected Frame determineNextFrame(Frame frame) {
        return shouldJump ? frame.jumpTo(jumpLabel) : frame.advance();
    }

    @Override
    protected String getOperandsString() {
        return jumpLabel.toString();
    }
}
