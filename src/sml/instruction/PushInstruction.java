package sml.instruction;
import sml.*;

import java.util.Optional;
/**
 * Represents the 'push' instruction in the Simple Machine Language (SML).
 * The push instruction adds a constant integer value directly onto the current frame's operand stack.
 *  ===================================================================================
 * @author Ricki Angel
 */



public class PushInstruction extends Instruction {
    public static final String OP_CODE = "push";
    private final int value;

    /**
     * Constructs a new PushInstruction with an optional label and a specific integer value.
     *
     * @param label an optional label for the instruction (can be null)
     * @param value the integer value to be pushed onto the operand stack
     */

    /**
     * Constructor for the Store instruction class.
     * ==========================================
     *
     * @param label optional label (can be null)
     * @throws NullPointerException if varName is null // TODO: Might not be needed. Investigate.
     */
    public PushInstruction(Label label, String opcode, int value) {
        super(label, opcode);
        this.value = value;
    }

    // ==================================== Methods ======================================

    @Override
    public Optional<Frame> execute(Machine machine) {
        return Optional.empty();
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
