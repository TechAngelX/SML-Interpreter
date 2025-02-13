package sml.instruction;
import sml.*;

import java.util.Optional;
/**
 * Represents the 'push' instruction in the Simple Machine Language (SML).
 * Adds a constant integer value directly onto the current frame's operand stack.
 *  ===================================================================================
 *
 * @author Ricki Angel
 */



public class PushInstruction extends Instruction {
    public static final String OP_CODE = "push";
    private final int value;

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
