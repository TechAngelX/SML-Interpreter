package sml.instruction;

import sml.*;

import java.util.Optional;
/**
 * ================================================================
 * Return instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Handles method return mechanism by popping the top value
 * from the current frame and pushing it to the invoking frame.
 *
 * Manages method call stack and value propagation between method contexts.
 *
 * @author Ricki Angel
 */
public class ReturnInstruction extends Instruction {

    public static final String OP_CODE = "return";

    public ReturnInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value = frame.pop();
        Optional<Frame> optionalInvoker = frame.invoker();
        if (optionalInvoker.isPresent()) {
            Frame invoker = optionalInvoker.get();
            invoker.push(value);
            return Optional.of(invoker.advance());
        }
        return optionalInvoker;
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
