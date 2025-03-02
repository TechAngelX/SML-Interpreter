package sml.instructions;

import sml.Label;
import sml.Variable;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Abstract base class for SML instructions that interact with variables.
 *
 * <p>Provides a common implementation for instructions that manipulate variables, 
 * such as load and store operations. Ensures consistent variable name management 
 * across different variable-based instruction types.</p>
 *
 * <h2>Key Responsibilities<:/h2>
 * <ul>
 *   <li>Define a standard interface for variable-based instructions</li>
 *   <li>Manage variable identifiers consistently</li>
 *   <li>Provide default implementations for equality and hash code generation</li>
 *   <li>Support stream-based variable retrieval</li>
 * </ul>
 *
 * <p>Subclasses must extend this class to inherit standard variable instruction behaviors.</p>
 *
 * @author Ricki Angel
 */
public abstract  class AbstractVarInstruction extends Instruction
       {

    /**
     * The identifier for the variable being manipulated.
     */
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
