package sml.instructions;

import sml.Label;
import sml.Variable;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Abstract base class for instructions that manipulate variables.
 * <p>
 * This class provides common functionality for instructions that interact with variables,
 * such as load and store operations. It is extended by specific instructions that involve
 * variable manipulation.
 * </p>
 * <p>
 * The class defines a common representation for variable-based instructions, ensuring that
 * variable names are managed consistently.
 * </p>
 *
 * @author Ricki Angel
 */
public abstract sealed class AbstractVarInstruction extends Instruction
        permits LoadInstruction, StoreInstruction {

    /** The identifier for the variable being manipulated. */
    protected final Variable.Identifier varName;

    /**
     * Constructs an {@code AbstractVarInstruction} with the given label, opcode, and variable name.
     *
     * @param label   the label associated with this instruction
     * @param opcode  the operation code of the instruction
     * @param varName the identifier of the variable being manipulated
     * @throws NullPointerException if {@code varName} is null
     */
    protected AbstractVarInstruction(Label label, String opcode, Variable.Identifier varName) {
        super(label, opcode);
        this.varName = Objects.requireNonNull(varName, "Variable identifier cannot be null");
    }

    /**
     * Returns a stream containing the variable involved in this instruction.
     *
     * @return a stream containing a single variable identifier
     */
    @Override
    public Stream<Variable.Identifier> variables() {
        return Stream.of(varName);
    }

    /**
     * Checks whether this instruction is equal to another object.
     *
     * @param o the object to compare
     * @return {@code true} if this instruction is equal to {@code o}, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractVarInstruction that = (AbstractVarInstruction) o;
        return Objects.equals(varName, that.varName);
    }

    /**
     * Computes the hash code for this instruction.
     *
     * @return the computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), varName);
    }

    /**
     * Returns a string representation of the operand(s) for this instruction.
     *
     * @return the variable name as a string
     */
    @Override
    protected String getOperandsString() {
        return varName.toString();
    }


}