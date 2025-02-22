package sml.instruction;

// TODO: write JavaDoc for the class

import sml.Frame;
import sml.Label;
import sml.Machine;
import sml.Variable;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
/**
 * ================================================================
 * Abstract base class for instructions in the Simple Machine Language.
 * ================================================================
 *
 * Defines the core structure and behavior for all language instructions,
 * including execution, labeling, and comparison mechanisms.
 *
 * @author Ricki Angel
 */
public abstract class Instruction {
    protected final Label label;
    protected final String opcode;

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label optional label (can be null)
     * @param opcode operation name
     */
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
     * Executes the instruction in the given machine.
     *
     * @param machine the machine the instruction runs on
     * @return the new frame with an update instruction index
     */

    public abstract Optional<Frame> execute(Machine machine);

    /**
     * Returns a string representation of the operands.
     * Used in toString().
     *
     * @return a string representation of the operands
     */

    protected abstract String getOperandsString();

    /**
     * Compares this instruction with another object for equality, and/or null.
     * Subclasses should override this method to include their specific fields.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
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
     * Generates a hash code incorprating just label and opcode for this instruction
     * and its subclasses.
     * Objects that are considered equal should have the same hashCode.
     * Subclasses MAY override this method to include their specific fields.
     *
     * @return a hash code value for this instruction
     */
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
