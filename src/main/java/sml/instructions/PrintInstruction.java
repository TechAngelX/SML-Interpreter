package sml.instructions;

import sml.*;

import java.util.Optional;

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
public class PrintInstruction extends Instruction {
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
     * Executes the print instruction's core functionality.
     *
     * <p>Outputs the top value from the operand stack to the system console, 
     * supporting runtime program state visualization and debugging.</p>
     *
     * <p>Key output behavior:</p>
     * <ul>
     *   <li>Retrieves the topmost integer value from the execution frame's stack</li>
     *   <li>Immediately prints the value to standard output</li>
     *   <li>Supports diagnostic and monitoring capabilities during program execution</li>
     * </ul>
     *
     * <p>Utilises Optional and method reference for concise, null-safe value extraction.</p>
     *
     * @param frame The current stack-based execution context containing the value to print
     */
    @Override
    protected void doExecute(Frame frame) {
        Optional.of(frame.pop())  
                .ifPresent(System.out::println); 
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
