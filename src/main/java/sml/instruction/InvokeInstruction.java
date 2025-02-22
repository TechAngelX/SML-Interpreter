package sml.instruction;

import sml.*;

import java.util.Objects;
import java.util.Optional;

/**
 * ================================================================
 * Invoke instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Initiates method invocation by creating a new frame
 * for the specified method.
 *
 * Enables method call and context switching mechanisms
 * during program execution.
 *
 * @author Ricki Angel
 */
public class InvokeInstruction extends Instruction {

    public static final String OP_CODE = "invoke";

    private final Method.Identifier methodName;

    public InvokeInstruction(Label label, Method.Identifier methodName) {
        super(label, OP_CODE);
        this.methodName = Objects.requireNonNull(methodName);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        return machine.newFrameForMethodInvocation(methodName);
    }

    @Override
    protected String getOperandsString() {
        return "@" + methodName;
    }
}
