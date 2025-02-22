package sml.instruction;

import sml.*;
import java.util.Optional;

/**
 * ================================================================
 * Equal Comparison and Goto instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Pops two values from stack and compares them for equality.
 * If values are equal, jumps to specified label;
 * otherwise, continues to next instruction.
 *
 * Provides conditional branching based on stack value comparison.
 *
 * @author Ricki Angel
 */
public class IfEqualGotoInstruction extends Instruction {
    public static final String OP_CODE = "if_cmpeq";
    private final Label jumpLabel;

    public IfEqualGotoInstruction(Label label, Label jumpLabel) {
        super(label, OP_CODE);
        this.jumpLabel = jumpLabel;
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value2 = frame.pop(); // Remember LIFO pop order.
        int value1 = frame.pop();

        if (value1 == value2) {
            return Optional.of(frame.jumpTo(jumpLabel));
        } else {

            return Optional.of(frame.advance());
        }
    }

    @Override
    protected String getOperandsString() {
        return jumpLabel.toString();
    }
}