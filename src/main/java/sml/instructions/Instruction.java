package sml.instructions;

import sml.Frame;
import sml.Label;
import sml.Machine;
import sml.Variable;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents the base instruction type in the SML runtime environment.
 *
 * <p>The Instruction class provides a common framework for all SML instructions, with:</p>
 * <ul>
 *   <li>Template method pattern for execution flow control</li>
 *   <li>Label identification and management</li>
 *   <li>Operand handling and representation</li>
 *   <li>Consistent execution semantics across instruction types</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Standardizes instruction execution sequence</li>
 *   <li>Manages program flow transitions between instructions</li>
 *   <li>Provides extension points for instruction-specific behavior</li>
 *   <li>Supports instruction introspection and debugging</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public abstract sealed class Instruction permits AbstractVarInstruction, AddInstruction, DivInstruction, GotoInstruction, IfCmpeqInstruction, IfCmpgtInstruction, InvokeInstruction, MulInstruction, NotEqInstruction, PrintInstruction, PushInstruction, ReturnInstruction, SqrtInstruction, SubInstruction {
    protected final Label label;
    protected final String opcode;

    /**
     * Constructs a new Instruction with specified label and opcode.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional label for branching targets</li>
     *   <li>A required operation code defining instruction type</li>
     * </ul>
     *
     * @param label Optional label identifying this instruction location
     * @param opcode Required operation code indicating instruction type
     * @throws NullPointerException if opcode is null
     */
    public Instruction(Label label, String opcode) {
        this.label = label;
        this.opcode = Objects.requireNonNull(opcode);
    }

    /**
     * Returns an Optional containing this instruction's label.
     *
     * <p>Provides safe access to the instruction's label which may be null.</p>
     *
     * @return Optional containing the label, or empty if no label
     */
    public Optional<Label> optionalLabel() {
        return Optional.ofNullable(label);
    }

    /**
     * Returns this instruction's operation code.
     *
     * <p>The opcode uniquely identifies the instruction type in the SML system.</p>
     *
     * @return String representation of the instruction's opcode
     */
    public String opcode() {
        return opcode;
    }

    /**
     * Returns the stream of variables in the instruction's operands.
     *
     * <p>Provides access to variable identifiers referenced by this instruction.
     * Default implementation returns empty stream. Subclasses with variable
     * operands must override.</p>
     *
     * @return Stream of variable identifiers referenced by this instruction
     */
    public Stream<Variable.Identifier> variables() {
        return Stream.of();
    }

    /**
     * Executes this instruction in the given machine context.
     *
     * <p>Template method that defines execution sequence:</p>
     * <ul>
     *   <li>Performs instruction-specific execution logic</li>
     *   <li>Determines the next frame to execute</li>
     *   <li>Returns the next frame wrapped in Optional</li>
     * </ul>
     *
     * @param machine The machine the instruction runs on
     * @return Optional containing the next frame, or empty if execution terminates
     */
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        doExecute(frame);
        Frame nextFrame = determineNextFrame(frame);

        if (nextFrame == null) {
            // Handle this case, possibly by returning an empty Optional or logging the error:
            return Optional.empty();
        }

        return Optional.of(nextFrame);
    }

    /**
     * Performs instruction-specific execution logic.
     *
     * <p>Hook method that must be implemented by concrete subclasses
     * to provide the core behavior of the instruction.</p>
     *
     * @param frame Current execution frame
     */
    protected abstract void doExecute(Frame frame);

    /**
     * Determines the next frame after instruction execution.
     *
     * <p>Template method that controls program flow:</p>
     * <ul>
     *   <li>Default implementation advances to next sequential instruction</li>
     *   <li>Subclasses override for special flow control (jumps, returns)</li>
     * </ul>
     *
     * @param frame Current execution frame
     * @return Next frame to execute
     */
    protected Frame determineNextFrame(Frame frame) {
        return frame.advance();
    }

    /**
     * Returns a string representation of the instruction's operands.
     *
     * <p>Must be implemented by concrete subclasses to provide
     * string representation of instruction-specific operands.</p>
     *
     * @return String representation of operands
     */
    protected abstract String getOperandsString();

    /**
     * Compares this instruction with another object for equality.
     *
     * <p>Two instructions are equal if they:</p>
     * <ul>
     *   <li>Are of the same concrete class</li>
     *   <li>Have equal labels (or both have no label)</li>
     *   <li>Have equal opcodes</li>
     * </ul>
     *
     * @param o Object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(label, that.label) &&
                Objects.equals(opcode, that.opcode);
    }

    /**
     * Computes a hash code for this instruction.
     *
     * <p>The hash code is based on the instruction's label and opcode.</p>
     *
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(label, opcode);
    }

    /**
     * Returns a string representation of this instruction.
     *
     * <p>Format includes:</p>
     * <ul>
     *   <li>Label (if present) followed by colon</li>
     *   <li>Opcode</li>
     *   <li>String representation of operands</li>
     * </ul>
     *
     * @return String representation of the instruction
     */
    @Override
    public String toString() {
        return String.join(" ",
                optionalLabel().map(s -> s + ":").orElse(""),
                opcode(),
                getOperandsString());
    }
}