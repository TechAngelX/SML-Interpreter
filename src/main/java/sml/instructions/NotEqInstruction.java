package sml.instructions;

import sml.*;

import java.util.Objects;

/**
 * Not Equal instruction for the SML instruction set. (supplementary - custom instruction set).
 *
 * <p>This class is an additional test mock-up instruction designed as a test case
 * for the dynamic instruction discovery system (both config and package scan). This 
 * is supplementary to the SDP coursework assignment, designed purely to enhance my 
 * familiarity with opcode and instruction discovery.</p>
 *
 * <p>The NotEqInstruction class implements a conditional branch operation that:</p>
 * <ul>
 *   <li>Pops two values from the operand stack</li>
 *   <li>Compares them for inequality</li>
 *   <li>Transfers control to a target label if the values are not equal</li>
 *   <li>Continues sequential execution otherwise</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Provides conditional control flow manipulation based on inequality</li>
 *   <li>Enables dynamic decision-making based on runtime values</li>
 *   <li>Supports implementation of if-then-else constructs and loops</li>
 *   <li>Demonstrates extension of the instruction set through dynamic discovery</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class NotEqInstruction extends Instruction {
    public static final String OP_CODE = "not_eq";
    private Label jumpLabel;
    private boolean shouldJump;

    /**
     * Constructs a new NotEqInstruction with only a label.
     * This constructor exists to support instruction factory testing.
     *
     * @param label The label identifying this instruction (can be null)
     */
    public NotEqInstruction(Label label) {
        super(label, OP_CODE);
    }

    /**
     * Constructs a new NotEqInstruction with specified label and jump target.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>A required target label for conditional branching</li>
     * </ul>
     *
     * @param label     The label identifying this instruction (can be null)
     * @param jumpLabel The target label to jump to if condition is met
     * @throws NullPointerException if jumpLabel is null
     */
    public NotEqInstruction(Label label, Label jumpLabel) {
        super(label, OP_CODE);
        this.jumpLabel = Objects.requireNonNull(jumpLabel, "Jump target label cannot be null");
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the inequality comparison by:</p>
     * <ul>
     *   <li>Popping two values from the operand stack</li>
     *   <li>Comparing them for inequality</li>
     *   <li>Storing the comparison result for branch determination</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        if (jumpLabel == null) {
            int value2 = frame.pop();
            int value1 = frame.pop();
            frame.push(value1 != value2 ? 1 : 0);
        } else {
            int value2 = frame.pop();
            int value1 = frame.pop();
            shouldJump = (value1 != value2);
        }
    }

    /**
     * Determines the next frame after instruction execution.
     *
     * <p>Controls program flow by:</p>
     * <ul>
     *   <li>Jumping to the target label if values were not equal</li>
     *   <li>Advancing to the next sequential instruction otherwise</li>
     * </ul>
     *
     * @param frame The current execution frame
     * @return The next frame to execute
     */
    @Override
    protected Frame determineNextFrame(Frame frame) {
        return (jumpLabel != null && shouldJump) ? frame.jumpTo(jumpLabel) : frame.advance();
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Formats the jump target label for program display and debugging.</p>
     *
     * @return String representation of the target label or empty if no jump target
     */
    @Override
    protected String getOperandsString() {
        return jumpLabel != null ? jumpLabel.toString() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NotEqInstruction that = (NotEqInstruction) o;
        return Objects.equals(jumpLabel, that.jumpLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), jumpLabel);
    }
}
