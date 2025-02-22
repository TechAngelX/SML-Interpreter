package sml.instruction;
import sml.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the 'push' instruction in the Simple Machine Language (SML).
 * Pushes a constant integer value directly onto the current frame's operand stack.
 * ================================================================================
 *
 * @author Ricki Angel
 */
public class PushInstruction extends Instruction {
    public static final String OP_CODE = "push";
    private final int value;

    /**
     * Constructor for the Push instruction class.
     *
     * @param label - an Optional label (we can use * Optional.empty() if absent)
     * @param value the constant integer value to push onto the stack
     */
    public PushInstruction(Label label, int value) {
        super(label, OP_CODE);
        this.value = value;
    }
    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        frame.push(value);
        System.out.println("Result: " + value);
        return Optional.of(frame.advance());
    }

    @Override
    protected String getOperandsString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        PushInstruction that = (PushInstruction) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}