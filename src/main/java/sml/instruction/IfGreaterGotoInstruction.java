package sml.instruction;

import sml.Frame;
import sml.Instruction;
import sml.Label;
import sml.Machine;

import java.util.Optional;

/**
 * Compare amd Goto instruction for SML.
 * Pops two values from the stack and compares them.
 * If the first value is greater than the second, jumps to a
 * specified label; else, continues to next instruction.
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