package sml.instructions;

import sml.*;

/**
 * A Modulo instruction in the SML runtime environment (Supplementary).
 *
 * <p>This class is an additional test mock-up instruction designed as a test case
 * for the dynamic instruction discovery system (both config and package scan). This 
 * is supplementary to the SDP coursework assignment, designed purely to enhance my 
 * familiarity with opcode and instruction discovery.</p>
 *  
 * <p>The ModInstruction class implements arithmetic operations that:</p>
 * <ul>
 *   <li>Pop two integer values from the operand stack</li>
 *   <li>Calculate the remainder when the first value is divided by the second</li>
 *   <li>Push the resulting remainder back onto the stack</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Performs integer modulo operations</li>
 *   <li>Serves as a test case for the instruction factory system to test true extensibility</li>
 *   <li>Supports modular arithmetic in SML programs</li>
 *   <li>Handles error conditions like division by zero</li>
 * </ul>
 *
 * <p>Note: The modulo operation in this instruction follows Java's semantics,
 * where the sign of the result matches the sign of the dividend.</p>
 *
 * @author Ricki Angel
 */
public class ModInstruction extends Instruction {
    public static final String OP_CODE = "mod";

    /**
     * Constructs a {@code ModInstruction} with the given label.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>The modulo operation code</li>
     * </ul>
     *
     * @param label the label associated with this instruction (can be null)
     */
    public ModInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the modulo operation.
     * <p>
     * Pops the top two values from the stack, calculates the remainder when
     * the first value is divided by the second, and pushes the result back onto the stack.
     * </p>
     * <p>
     * Throws an {@link ArithmeticException} if division by zero is attempted.
     * </p>
     *
     * @param frame the current execution frame
     * @throws ArithmeticException if the divisor is zero
     */
    @Override
    protected void performInstructionLogic(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        if (value2 == 0) {
            throw new ArithmeticException("Modulo by zero");
        }
        frame.push(value1 % value2);
    }

    /**
     * Returns a string representation of the operands.
     *
     * <p>Modulo requires no additional operands beyond
     * the values obtained from the stack.</p>
     *
     * @return Empty string as this instruction has no operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
