package sml.instructions;

import sml.*;

import java.util.Objects;

/**
 * Represents the unconditional jump instruction in the SML runtime environment.
 *
 * <p>The GotoInstruction class implements an unconditional branch operation that:</p>
 * <ul>
 *   <li>Transfers execution to a specified target label</li>
 *   <li>Requires no conditions to be evaluated</li>
 *   <li>Alters normal sequential flow of instruction execution</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Provides direct control flow manipulation</li>
 *   <li>Enables implementation of loops and branching structures</li>
 *   <li>Maintains program execution context during jumps</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class GotoInstruction extends Instruction {
    public static final String OP_CODE = "goto";
    private final Label branchLabel;

    /**
     * Constructs a new GotoInstruction with specified label and target.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>A required target label where execution will jump to</li>
     * </ul>
     *
     * @param label       The label identifying this instruction (can be null)
     * @param branchLabel The target label to jump to
     * @throws NullPointerException if branchLabel is null
     */
    public GotoInstruction(Label label, Label branchLabel) {
        super(label, OP_CODE);
        this.branchLabel = Objects.requireNonNull(branchLabel);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>For GotoInstruction, no computation is required during execution
     * as the instruction only affects control flow.</p>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        // No operation needed - goto only changes control flow in determineNextFrame()
    }

    /**
     * Determines the next frame after instruction execution.
     *
     * <p>Changes the program flow by:</p>
     * <ul>
     *   <li>Locating the target instruction via the branch label</li>
     *   <li>Setting the program counter to the target instruction position</li>
     * </ul>
     *
     * @param frame The current execution frame
     * @return The modified frame with updated program counter
     */
    @Override
    protected Frame determineNextFrame(Frame frame) {
        return frame.jumpTo(branchLabel);
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Formats the branch target label for program display and debugging.</p>
     *
     * @return String representation of the target label
     */
    @Override
    protected String getOperandsString() {
        return branchLabel.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GotoInstruction that = (GotoInstruction) o;
        return Objects.equals(branchLabel, that.branchLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), branchLabel);
    }


}
