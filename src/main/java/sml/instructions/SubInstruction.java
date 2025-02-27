package sml.instructions;

import sml.*;

/**
 * A Subtraction instruction for the SML runtime environment.
 *
 * <p>The SubInstruction class implements arithmetic operations that:</p>
 * <ul>
 *   <li>Pop two integer values from the operand stack</li>
 *   <li>Subtract the second value from the first value</li>
 *   <li>Push the resulting difference back onto the stack</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Performs integer subtraction</li>
 *   <li>Manages operand stack interactions</li>
 *   <li>Supports arithmetic expression evaluation</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class SubInstruction extends Instruction {
    public static final String OP_CODE = "sub";

    /**
     * Constructs a new SubInstruction with the specified label.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>The subtraction operation code</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     */
    public SubInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the subtraction by:</p>
     * <ul>
     *   <li>Popping two values from the operand stack</li>
     *   <li>Subtracting the second value from the first value</li>
     *   <li>Pushing the difference back onto the stack</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(value1 - value2);
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Subtraction requires no additional operands beyond
     * the values obtained from the stack.</p>
     *
     * @return Empty string as this instruction has no operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
