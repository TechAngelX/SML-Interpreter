package sml.instruction;

import sml.*;
import java.util.Optional;

/**
 * Equal comparison and Goto instruction for SML.
 * Pops two values from stack and compares them.
 * If values are equal, jumps to specified label;
 * else, continue to next instruction.
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

        if (value1 == value2) {   // If values are equal, jump to specified label.
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