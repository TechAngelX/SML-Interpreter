package sml.instruction;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import sml.*;
/**
 * This class represents the 'load' instruction from my Simple Machine Language.
 * The load instruction retrieves a value from a variable (either a method argument or
 * local variable), and pushes it onto the current operand stack.
 * ===================================================================================
 *
 * @author Ricki Angel
 */
public class LoadInstruction extends Instruction {
    public static final String OP_CODE = "load";

    private final Variable.Identifier varName;

    /**
     * Constructor for the Load instruction class.
     * ==========================================
     *
     * @param label optional label (can be null)
     * @param varName the identifier of the variable to load (must not be null)
     * @throws NullPointerException if varName is null
     */
    public LoadInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE);
        this.varName = Objects.requireNonNull(varName);
    }
// ==================================== Methods ======================================
    /**
     * Overrides from the Instruction superclass.
     * Retrieves the value (varName) from the current frame, pushes that value onto the
     * top of the operand stack, prints it and advances the pointer.
     *
     * @param machine the machine the instruction runs on
     * @return An Optional containing the next frame after execution
     * @throws  VariableNotFoundException if the specified variable is not found
     */
    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        Variable var = frame.variable(varName); // Get the Variable object
        int value = var.load();                 // Use the load() method in Variable to get the value
        frame.push(value);                      // Push the value onto stack
        System.out.println(value);
        return Optional.of(frame.advance());
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

    /** variables()
     * Returns a stream containng the variable used in this instruction.
     *
     * @return a stream containing just one variable - the one this instruction stores into
     */
    @Override
    public Stream<Variable.Identifier> variables() {
        return Stream.of(varName);
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