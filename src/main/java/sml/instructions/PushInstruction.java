package sml.instructions;

import sml.*;

import java.util.Objects;

/**
 * Represents the constant value loading instruction in the SML runtime environment.
 *
 * <p>The PushInstruction class implements stack operations that:</p>
 * <ul>
 *   <li>Push literal integer values onto the operand stack</li>
 *   <li>Provide immediate data for subsequent operations</li>
 *   <li>Support constant value usage in computations</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Introduces constant values into program execution</li>
 *   <li>Manages operand stack additions</li>
 *   <li>Supports immediate value operations</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class PushInstruction extends Instruction {
    public static final String OP_CODE = "push";
    private final int value;

    /**
     * Constructs a new PushInstruction with the specified label and value.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>A required integer value to push onto the stack</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     * @param value The integer value to push onto the stack
     */
    public PushInstruction(Label label, int value) {
        super(label, OP_CODE);
        this.value = value;
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the push operation by:</p>
     * <ul>
     *   <li>Taking the instruction's constant value</li>
     *   <li>Pushing it onto the frame's operand stack</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void performInstructionLogic(Frame frame) {
        frame.push(value);
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Formats the constant value for program display and debugging.</p>
     *
     * @return String representation of the constant value
     */
    @Override
    protected String getOperandsString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PushInstruction that = (PushInstruction) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
