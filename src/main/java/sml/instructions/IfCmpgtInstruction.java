package sml.instructions;

import sml.*;

import java.util.Objects;

/**
 * A conditional greater-than comparison instruction.
 *
 * <p>The IfCmpgtInstruction class implements a conditional branch operation that:</p>
 * <ul>
 *   <li>Pops two values from the operand stack</li>
 *   <li>Compares if the first value is greater than the second</li>
 *   <li>Transfers control to a target label if the condition is true</li>
 *   <li>Continues sequential execution otherwise</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Provides conditional branching based on numeric comparison</li>
 *   <li>Enables implementation of comparison-based control structures</li>
 *   <li>Supports dynamic decision points in program execution</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public non-sealed class IfCmpgtInstruction extends Instruction {
    public static final String OP_CODE = "if_cmpgt";
    private final Label jumpLabel;
    private boolean shouldJump;

    /**
     * Constructs a new IfCmpgtInstruction with specified label and jump target.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>A required target label for conditional branching</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     * @param jumpLabel The target label to jump to if condition is met
     */
    public IfCmpgtInstruction(Label label, Label jumpLabel) {
        super(label, OP_CODE);
        this.jumpLabel = jumpLabel;
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the greater-than comparison by:</p>
     * <ul>
     *   <li>Popping two values from the operand stack</li>
     *   <li>Comparing if first value is greater than second</li>
     *   <li>Storing the comparison result for branch determination</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        int value2 = frame.pop();
        int value1 = frame.pop();
        shouldJump = (value1 > value2);
    }

    /**
     * Determines the next frame after instruction execution.
     *
     * <p>Controls program flow by:</p>
     * <ul>
     *   <li>Jumping to the target label if comparison was true</li>
     *   <li>Advancing to the next sequential instruction otherwise</li>
     * </ul>
     *
     * @param frame The current execution frame
     * @return The next frame to execute
     */
    @Override
    protected Frame determineNextFrame(Frame frame) {
        return shouldJump ? frame.jumpTo(jumpLabel) : frame.advance();
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Formats the jump target label for program display and debugging.</p>
     *
     * @return String representation of the target label
     */
    @Override
    protected String getOperandsString() {
        return jumpLabel.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IfCmpgtInstruction that = (IfCmpgtInstruction) o;
        return Objects.equals(jumpLabel, that.jumpLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), jumpLabel);
    }
}