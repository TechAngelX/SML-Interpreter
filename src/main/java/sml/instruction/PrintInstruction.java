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
public class PrintInstruction extends Instruction {

    public static final String OP_CODE = "print";

    public PrintInstruction(Label label) {
        super(label, OP_CODE);

    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value = frame.pop();
        System.out.println(value);
        return Optional.of(frame.advance());
    }
    @Override
    protected String getOperandsString() {
        return "";
    }
}
