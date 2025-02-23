package sml.instructions;

import sml.*;

/**
 * ====================================================================================================================
 * Square Root instruction for Simple Machine Language (SML).
 * --------------------------------------------------------------------------------------------------------------------
 * A bespoke instruction class to test injecting new future instructions into the InstructionFactory.
 * Pops a value from the stack, calculates its square root, and pushes the integer result back onto the stack.
 * Provides a test case for dynamic instruction extension in the Simple Machine Language interpreter.
 * ====================================================================================================================
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */

public class SqrtInstruction extends Instruction {
    public static final String OP_CODE = "sqrt";

    public SqrtInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value = frame.pop();
        double result = Math.sqrt(value);
        frame.push((int) result);
    }

    @Override
    protected String getOperandsString() {
        return "";
    }
}