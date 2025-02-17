package sml.instruction;
import sml.*;
import sml.helperfiles.AbstractVarInstruction;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the 'load' instruction from the Simple Machine Language.
 * ================================================================

 * Retrieves a value from a variable (either a method argument or
 * local variable), and pushes it onto the current operand stack.
 *
 * @author Ricki Angel
 */
public class LoadInstruction extends AbstractVarInstruction {
    public static final String OP_CODE = "load";
    /**
     * Constructor for the Load instruction class.
     * ==========================================
     *
     * @param label optional label (can be null)
     * @param varName the identifier of the variable to load
     */

    public LoadInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE, varName);
    }

// ==================================== Methods ======================================

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        Variable var = frame.variable(varName); // Get the Variable object
        int value = var.load();                 // Use the load() method in Variable to get the value
        frame.push(value);                      // Push the value onto stack
        System.out.println(value);
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
        LoadInstruction other = (LoadInstruction) o;
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