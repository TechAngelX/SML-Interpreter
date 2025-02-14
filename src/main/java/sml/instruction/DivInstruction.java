package sml.instruction;

import sml.Frame;
import sml.Instruction;
import sml.Label;
import sml.Machine;

import java.util.Optional;

/**
 * Divide instruction for SML. Takes two numbers from stack,
 * divides them, and pushes result back.
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

        // Handle division by zero
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