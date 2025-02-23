package sml.instructions;

import sml.*;

/**
 * =====================================================================================
 * Greater Than Comparison and Goto instruction for Simple Machine Language (SML).
 * -------------------------------------------------------------------------------------
 * <p>
 * Pops two values from stack and compares them.If the first value is greater than the
 * second, jumps to specified label; otherwise, continues to next instruction.
 * <p>
 * Provides conditional branching based on stack value comparison.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */
public class IfCmpgtInstruction extends Instruction {
    public static final String OP_CODE = "if_cmpgt";
    private final Label jumpLabel;
    private boolean shouldJump;

    public IfCmpgtInstruction(Label label, Label jumpLabel) {
        super(label, OP_CODE);
        this.jumpLabel = jumpLabel;
    }

    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        shouldJump = (value1 > value2);
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
