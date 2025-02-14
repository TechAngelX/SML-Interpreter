package sml.instruction;

import sml.Frame;
import sml.Instruction;
import sml.Label;
import sml.Machine;

import java.util.Optional;


/**
 * Represents the 'add' instruction in the Simple Machine Language (SML).
 * ======================================================================

 * Performs addition by removing the top two values from the stack,
 * summing them, and placing the result back onto the stack.
 * Follows the Last-In-First-Out (LIFO) stack manipulation principle.
 */

public class AddInstruction extends Instruction {
    public static final String OP_CODE = "add";


    /**
     * Constructor for the Add instruction class.
     * ==========================================
     *
     * @param label optional label (can be null)
     */
    public AddInstruction(Label label) {
        super(label, OP_CODE);
    }

    // ==================================== Methods ======================================

    /**
     * Adds two values from the top of the stack using Last-In-First-Out (LIFO) order.
     * ===============================================================================

     * Performs addition by popping the top two values from the stack,
     * summing them, and pushing the result back onto the stack.
     *
     * @param machine the machine containing the current execution frame
     * @return an Optional containing the next frame after instruction execution
     */
    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();

        int value1 = frame.pop();
        int value2 = frame.pop();

        frame.push(value1 + value2);
        return Optional.of(frame.advance());
    }


    @Override
    protected String getOperandsString() {
        return "";
    }
}
