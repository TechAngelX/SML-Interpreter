package sml.instruction;
import sml.*;
import sml.helperfiles.AbstractVarInstruction;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents the 'store' instruction from the Simple Machine Language.
 * ===================================================================

 * Pops a value from the current operand stack and stores it
 * in a specified variable (either a method argument or local variable).
 */

public class StoreInstruction extends AbstractVarInstruction {
    public static final String OP_CODE = "store";

    /**
     * Constructor for the Store instruction class.
     * ==========================================
     *
     * @param label optional label (can be null)
     */
    public StoreInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE, varName);
    }

    // ==================================== Methods ======================================

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
        int value = frame.pop();                   // Pop value from the stack
        Variable var = frame.variable(varName);    // Get the Variable object
        var.store(value);                          // Store the value in the variable using the Variable store() method.
        return Optional.of(frame.advance());       // Advance to the next instruction
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
