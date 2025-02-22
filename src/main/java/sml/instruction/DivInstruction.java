package sml.instruction;

import sml.Frame;
import sml.Label;
import sml.Machine;

import java.util.Optional;

/**
 * ================================================================
 * Division instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Takes two numbers from stack, divides them,
 * and pushes the result back onto the stack.
 *
 * Handles potential division by zero scenarios.
 *
 * @author Ricki Angel
 */
public class DivInstruction extends Instruction {
    public static final String OP_CODE = "div";

    public DivInstruction(Label label) {
        super(label, OP_CODE);
    }

    public DivInstruction(Label label, String opcode) {
        super(label, opcode);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value2 = frame.pop(); // Remember stack order !
        int value1 = frame.pop();

        if (value2 == 0) {
            throw new ArithmeticException("Division by zero");
        }

        frame.push(value1 / value2);
        return Optional.of(frame.advance());
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}