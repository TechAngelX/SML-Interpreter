package sml.instruction;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import sml.*;
/**
 * This class represents the 'store' instruction from the Simple Machine Language.
 * The store instruction pops a value from the current operand stack and stores it
 * in a specified variable (either a method argument or local variable).
 *
 * @author Ricki Angel
 */

public class StoreInstruction  extends Instruction {
    public static final String OP_CODE = "store";
    private final Variable.Identifier varName;
    /**
     * Constructor for the Store instruction class.
     * ==========================================
     *
     * @param label optional label (can be null)
     * @param varName the identifier of the variable to store the value in (must not be null)
     * @throws NullPointerException if varName is null
     */
    public StoreInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE);
        this.varName = Objects.requireNonNull(varName);
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
    @Override
    protected String getOperandsString() {
        return "";
    }

    /**
     * Returns a stream containing the variable used in this instruction.
     *
     * @return stream containing this instruction's variable identifier (varName)
     */
    @Override
    public Stream<Variable.Identifier> variables() {
        return Stream.of(varName);
    }



}
