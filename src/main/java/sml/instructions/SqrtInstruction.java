package sml.instructions;

import sml.*;

/**
 * A Square Root instruction (supplementary - custom instruction set).
 *
 * <p>This class is an additional test mock-up instruction designed as a test case
 * for the dynamic instruction discovery system (both config and package scan). This 
 * is supplementary to the SDP coursework assignment, designed purely to enhance my 
 * familiarity with opcode and instruction discovery.</p> 
 * <ul>
 *   <li>Pops an integer value from the operand stack</li>
 *   <li>Calculates its square root using Java's Math library</li>
 *   <li>Pushes the integer result back onto the stack (truncating decimal portion)</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Performs square root mathematical operations</li>
 *   <li>Demonstrates dynamic instruction extension capabilities</li>
 *   <li>Serves as a test case for the instruction factory system</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class SqrtInstruction extends Instruction {
    public static final String OP_CODE = "sqrt";

    /**
     * Constructs a new SqrtInstruction with the specified label.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>The square root operation code</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     */
    public SqrtInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the square root operation by:</p>
     * <ul>
     *   <li>Popping a value from the operand stack</li>
     *   <li>Calculating its square root</li>
     *   <li>Converting the result to an integer</li>
     *   <li>Pushing the integer result back onto the stack</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        int value = frame.pop();
        double result = Math.sqrt(value);
        frame.push((int) result);
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Square root requires no additional operands beyond
     * the value obtained from the stack.</p>
     *
     * @return Empty string as this instruction has no operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
