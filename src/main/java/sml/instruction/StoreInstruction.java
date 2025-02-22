package sml.instruction;
import sml.*;

import java.util.Objects;
import java.util.Optional;

/**
 * ================================================================
 * Store instruction for Simple Machine Language (SML).
 * ================================================================
 *
 * Pops a value from the current operand stack and stores it
 * in a specified variable (either a method argument or local variable).
 *
 * Allows preservation of computational results in named variables.
 *
 * @author Ricki Angel
 */
public class StoreInstruction extends AbstractVarInstruction {
    public static final String OP_CODE = "store";

     public StoreInstruction
             (Label label, Variable.Identifier varName) {
        super(label, OP_CODE, varName);
    }

    /**
     * Executes the store instruction on the given machine.
     *
     * Pops the top value from the current frame's operand stack
     * and stores it in the specified variable.
     *
     * @param machine the machine the instruction runs on
     * @return An Optional containing the next frame after execution
     * @throws VariableNotFoundException if the specified variable is not found
     */
    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        int value = frame.pop();
        Variable var = frame.variable(varName);
        var.store(value);
        return Optional.of(frame.advance());
    }

       /** equals()
     * Compares this instruction with another object for equality.
     * Two LoadInstructions are equal if they have the same label, opcode,
     * and variable name.
     *
     * @param o object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoreInstruction other = (StoreInstruction) o;
        return Objects.equals(label, other.label)
                && Objects.equals(opcode, other.opcode)
                && Objects.equals(varName, other.varName);
    }

    /** hashCode()
     * Overrides hashCode in superclass to ensures consistent hash values for object
     * comparisons.
     *
     * @return hash code incorporating the label, opcode, and variable name
     */
    @Override
    public int hashCode() {
        return Objects.hash(label, opcode, varName);
    }
}
