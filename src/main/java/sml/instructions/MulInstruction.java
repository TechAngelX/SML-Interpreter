package sml.instructions;

import sml.*;

/**
 * A Multiplication instruction in the SML runtime environment.
 *
 * <p>The MulInstruction class implements arithmetic operations that:</p>
 * <ul>
 *   <li>Pop two integer values from the operand stack</li>
 *   <li>Multiply them together</li>
 *   <li>Push the resulting product back onto the stack</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Performs integer multiplication</li>
 *   <li>Manages operand stack interactions</li>
 *   <li>Supports arithmetic expression evaluation</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class MulInstruction extends Instruction {
    public static final String OP_CODE = "mul";

    /**
     * Constructs a new MulInstruction with the specified label.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>The multiplication operation code</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     */
    public MulInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the multiplication by:</p>
     * <ul>
     *   <li>Popping two values from the operand stack</li>
     *   <li>Multiplying them together</li>
     *   <li>Pushing the product back onto the stack</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        frame.push(Math.multiplyExact(value1, value2)); 
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Multiplication requires no additional operands beyond
     * the values obtained from the stack.</p>
     *
     * @return Empty string as this instruction has no operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
