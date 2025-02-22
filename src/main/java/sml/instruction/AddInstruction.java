package sml.instruction;

import sml.*;
import java.util.Optional;

/**
 * ================================================================
 * Add instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Takes two numbers from stack, adds them together,
 * and pushes the result back onto the stack.
 *
 * @author Ricki Angel
 */
public class AddInstruction extends Instruction {
    public static final String OP_CODE = "add";

    /**
     * Creates a new Add instruction.
     * @param label optional instruction label
     */
    public AddInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(value1 + value2);
        return Optional.of(frame.advance());
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}