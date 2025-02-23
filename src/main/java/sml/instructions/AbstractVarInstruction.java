package sml.instructions;

import sml.Label;
import sml.Variable;

import java.util.Objects;
import java.util.stream.Stream;
/**
 * ===========================================================================
 * Abstract base class for instructions that manipulate variables.
 * ---------------------------------------------------------------------------
 * This class provides common functionality for instructions
 * that interact with variables, such as load and store operations.
 * It is extended by specific instructions that involve variable manipulation.
 *
 * @author Ricki Angel
 */
public abstract sealed class AbstractVarInstruction extends Instruction permits LoadInstruction, StoreInstruction{
    protected final Variable.Identifier varName;

    protected AbstractVarInstruction(Label label, String opcode, Variable.Identifier varName) {
        super(label, opcode);
        this.varName = Objects.requireNonNull(varName);
    }

    @Override
    public Stream<Variable.Identifier> variables() {
        return Stream.of(varName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractVarInstruction that = (AbstractVarInstruction) o;
        return Objects.equals(varName, that.varName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), varName);
    }

    @Override
    protected String getOperandsString() {
        return varName.toString();
    }
}


