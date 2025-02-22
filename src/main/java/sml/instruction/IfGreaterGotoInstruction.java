package sml.instruction;

import sml.Frame;
import sml.Label;
import sml.Machine;

import java.util.Optional;
/**
 * ================================================================
 * Greater Than Comparison and Goto instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Pops two values from stack and compares them.
 * If the first value is greater than the second, jumps to specified label;
 * otherwise, continues to next instruction.
 *
 * Provides conditional branching based on stack value comparison.
 *
 * @author Ricki Angel
 */
public class IfGreaterGotoInstruction extends Instruction {
    public static final String OP_CODE = "if_cmpgt";
    private final Label jumpLabel;

    public IfGreaterGotoInstruction(Label label, Label jumpLabel) {
        super(label, OP_CODE);
        this.jumpLabel = jumpLabel;
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value2 = frame.pop(); // Remember Important LIFO pop order.
        int value1 = frame.pop();

        if (value1 > value2) {   // If value1 > value2, jump to specified label.
            return Optional.of(frame.jumpTo(jumpLabel));
        } else {
            // Otherwise, advance to next instruction.
            return Optional.of(frame.advance());
        }
    }

    @Override
    protected String getOperandsString() {
        return jumpLabel.toString();
    }
}