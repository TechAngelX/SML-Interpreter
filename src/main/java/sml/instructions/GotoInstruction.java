package sml.instructions;
import sml.*;

import java.util.Objects;

/**
 * =======================================================================================
 * Goto instruction for Simple Machine Language (SML).
 * ---------------------------------------------------------------------------------------
 * * Implements an unconditional jump to a specified label, altering the program's
 * control flow. Execution proceeds from the instruction associated with the target label.
 * <p>
 * This instruction facilitates direct control flow manipulation, enabling loops and other
 * non-sequential execution patterns.
 *
 * @author Ricki Angel
 */
public class GotoInstruction extends Instruction {
    public static final String OP_CODE = "goto";
    private final Label branchLabel;

    public GotoInstruction(Label label, Label branchLabel) {
        super(label, OP_CODE);
        this.branchLabel = Objects.requireNonNull(branchLabel);
    }

    @Override
    protected void doExecute(Frame frame) {
        // No-op: This subclass does not require execution logic.
    }

    @Override
    protected Frame determineNextFrame(Frame frame) {
        return frame.jumpTo(branchLabel);
    }

    @Override
    protected String getOperandsString() {
        return branchLabel.toString();
    }
}
