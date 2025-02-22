package sml.instruction;

import sml.Frame;
import sml.Label;
import sml.Machine;

import java.util.Optional;

/**
 * ================================================================
 * Multiplication instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Takes two numbers from stack, multiplies them together,
 * and pushes the result back onto the stack.
 *
 * @author Ricki Angel
 */
public class MultiplyInstruction extends Instruction {
    public static final String OP_CODE = "mul";

    public MultiplyInstruction(Label label) {
        super(label, OP_CODE);
    }

    public MultiplyInstruction(Label label, String opcode) {
        super(label, opcode);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(value1 * value2);
        return Optional.of(frame.advance());
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}