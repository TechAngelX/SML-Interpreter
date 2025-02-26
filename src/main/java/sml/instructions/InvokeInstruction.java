package sml.instructions;

import sml.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the method invocation instruction in the SML runtime environment.
 *
 * <p>The InvokeInstruction class implements method call operations that:</p>
 * <ul>
 *   <li>Create a new execution frame for a target method</li>
 *   <li>Transfer operand stack values as method arguments</li>
 *   <li>Switch execution context to the invoked method</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Initiates method calls during program execution</li>
 *   <li>Manages execution context transitions between methods</li>
 *   <li>Supports modular program structure and reuse</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public non-sealed class InvokeInstruction extends Instruction {
    public static final String OP_CODE = "invoke";
    private final Method.Identifier methodName;
    private Optional<Frame> newFrame;

    /**
     * Constructs a new InvokeInstruction with specified label and method name.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>A required method identifier for the target method</li>
     * </ul>
     *
     * @param label      The label identifying this instruction (can be null)
     * @param methodName The identifier of the method to invoke
     * @throws NullPointerException if methodName is null
     */
    public InvokeInstruction(Label label, Method.Identifier methodName) {
        super(label, OP_CODE);
        this.methodName = Objects.requireNonNull(methodName);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>For InvokeInstruction, no computation is required during execution
     * as the method invocation is handled in the execute method.</p>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        // No-op: This subclass does not require execution logic.
    }

    /**
     * Overrides the template method to perform method invocation.
     *
     * <p>Creates a new execution frame by:</p>
     * <ul>
     *   <li>Locating the target method in the program</li>
     *   <li>Creating a new frame for the method</li>
     *   <li>Transferring operand stack values as method arguments</li>
     * </ul>
     *
     * @param machine The machine the instruction runs on
     * @return Optional containing the new method frame
     */
    @Override
    public Optional<Frame> execute(Machine machine) {
        return machine.newFrameForMethodInvocation(methodName);
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Formats the method name with @ prefix for program display and debugging.</p>
     *
     * @return String representation of the method name
     */
    @Override
    protected String getOperandsString() {
        return "@" + methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InvokeInstruction that = (InvokeInstruction) o;
        return Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), methodName);
    }
}
