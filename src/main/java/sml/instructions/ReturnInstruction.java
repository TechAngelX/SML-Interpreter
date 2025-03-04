package sml.instructions;

import sml.*;

/**
 * Represents the method return instruction in the SML runtime environment.
 *
 * <p>The ReturnInstruction class implements method termination operations that:</p>
 * <ul>
 *   <li>Pop a value from the current frame's operand stack</li>
 *   <li>Transfer this value to the invoking frame's stack</li>
 *   <li>Transfer execution control back to the calling method</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Manages method call termination</li>
 *   <li>Handles return value propagation between frames</li>
 *   <li>Restores execution context to the calling method</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class ReturnInstruction extends Instruction {
    public static final String OP_CODE = "return";

    /**
     * Constructs a new ReturnInstruction with the specified label.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>The return operation code</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     */
    public ReturnInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the return operation by:</p>
     * <ul>
     *   <li>Popping a value from the current frame's stack</li>
     *   <li>Pushing this value onto the invoking frame's stack if present</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void performInstructionLogic(Frame frame) {
        int value = frame.pop();
        frame.invoker().ifPresent(invoker -> invoker.push(value));
    }

    /**
     * Determines the next frame after instruction execution.
     *
     * <p>Controls program flow by:</p>
     * <ul>
     *   <li>Retrieving the invoking frame</li>
     *   <li>Advancing the invoker's program counter</li>
     *   <li>Returning null if there is no invoker, indicating program termination</li>
     * </ul>
     *
     * @param frame The current execution frame
     * @return The invoking frame advanced to its next instruction, or null if none
     */
    @Override
    protected Frame determineNextFrame(Frame frame) {
        return frame.invoker()
                .map(Frame::advance)
                .orElse(null);
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Return instruction requires no additional operands beyond
     * the value obtained from the stack.</p>
     *
     * @return Empty string as this instruction has no operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
