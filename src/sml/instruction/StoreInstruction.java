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
    @Override
    public Optional<Frame> execute(Machine machine) {
        return Optional.empty(); //TODO - fix.
    }
    @Override
    protected String getOperandsString() {
        return "";
    }
}
