package sml.instruction;

import sml.Frame;
import sml.Label;
import sml.Machine;
import sml.Variable;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * =====================================================================================================
 * Abstract class representing an instruction in Simple Machine Language (SML).
 * -----------------------------------------------------------------------------------------------------
 * This class defines the general structure and behavior of SML instructions, providing a template for
 * specific instruction types. It manages the execution flow and operand handling, while allowing
 * subclass-specific execution logic through the template method pattern.
 * <p>
 * The class provides methods for: Label management, Opcode representation, Operand variable extraction
 * and Execution flow control.
 * <p>
 * Subclasses should implement the execution-specific logic in doExecute()
 * and provide the operands as a string in getOperandsString().
 *
 * @author Ricki Angel
 */

public abstract class Instruction {
    protected final Label label;
    protected final String opcode;


    public Instruction(Label label, String opcode) {
        this.label = label;
        this.opcode = Objects.requireNonNull(opcode);
    }

    public Optional<Label> optionalLabel() {
        return Optional.ofNullable(label);
    }

    public String opcode() {
        return opcode;
    }
    /**
     * Returns the stream of variables in the operands of the instruction.
     * This method must be overridden if the instruction has any variables.
     *
     * @return the stream of variables
     */
    public Stream<Variable.Identifier> variables() {
        return Stream.of();
    }

    /**
     * Template method that defines execution sequence.
     * This ensures consistent execution flow while allowing
     * instruction-specific behavior.
     *
     * @param machine the machine the instruction runs on
     * @return the new frame with an update instruction index
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
     * Hook method for instruction-specific execution logic.
     * Each concrete instruction must implement this.
     *
     * @param frame current execution frame
     */
    protected abstract void doExecute(Frame frame);

    /**
     * Template method to determine next frame. Default behavior advances to next instruction.
     * Override for special flow control (jumps, returns).
     *
     * @param frame current execution frame
     * @return next frame to execute
     */
    protected Frame determineNextFrame(Frame frame) {
        return frame.advance();
    }

    protected abstract String getOperandsString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(label, that.label) &&
                Objects.equals(opcode, that.opcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, opcode);
    }

    @Override
    public String toString() {
        return String.join(" ",
                optionalLabel().map(s -> s + ":").orElse(""),
                opcode(),
                getOperandsString());
    }
}