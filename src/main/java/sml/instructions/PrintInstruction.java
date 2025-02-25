package sml.instructions;

import sml.*;

/**
 * Print instruction in the SML runtime environment.
 *
 * <p>The PrintInstruction class implements output operations that:</p>
 * <ul>
 *   <li>Pop a value from the operand stack</li>
 *   <li>Display it on the console</li>
 *   <li>Support program monitoring and debugging</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Provides program output capabilities</li>
 *   <li>Enables runtime value inspection</li>
 *   <li>Facilitates debugging and result visualization</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public non-sealed class PrintInstruction extends Instruction {
    public static final String OP_CODE = "print";

    /**
     * Constructs a new PrintInstruction with the specified label.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>The print operation code</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     */
    public PrintInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the print operation by:</p>
     * <ul>
     *   <li>Popping a value from the operand stack</li>
     *   <li>Displaying it to the system console</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        int value = frame.pop();
        System.out.println(value);
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Print instruction requires no additional operands beyond
     * the value obtained from the stack.</p>
     *
     * @return Empty string as this instruction has no operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}