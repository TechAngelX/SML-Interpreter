package sml.helperfiles;

import sml.*;
import sml.instruction.Instruction;

import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractVarInstruction extends Instruction {
    protected final Variable.Identifier varName;

    protected AbstractVarInstruction(Label label, String opcode, Variable.Identifier varName) {
        super(label, opcode);
        this.varName = Objects.requireNonNull(varName);
    }


    /** variables()
     * Returns a stream containng the variable used in this instruction.
     *
     * @return a stream containing just one variable - the one this instruction stores into
     */
    @Override
    public Stream<Variable.Identifier> variables() {
        return Stream.of(varName);
    }

    /**
     * Returns a string version of the instruction's operands.
     *
     * @return the string of the variable name
     */
    @Override
    protected String getOperandsString() {
        return varName.toString();
    }


    // TODO equals and hashCode methods ...
}