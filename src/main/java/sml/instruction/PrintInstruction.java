package sml.instruction;

import sml.*;

/**
 * =====================================================================================
 * Print instruction for Simple Machine Language (SML).
 * -------------------------------------------------------------------------------------
 * <p>
 * Prints the top value from the current frame's operand stack to the console.
 * <p>
 * Displays values during program execution for debugging and output.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */
public class PrintInstruction extends Instruction {
    public static final String OP_CODE = "print";

    public PrintInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value = frame.pop();
        System.out.println(value);
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}
