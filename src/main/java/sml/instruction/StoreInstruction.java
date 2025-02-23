package sml.instruction;

import sml.*;

/**
 * ===================================================================================
 * Store instruction for Simple Machine Language (SML).
 * -----------------------------------------------------------------------------------
 * Pops a value from the current operand stack and stores it in a specified variable
 * (either a method argument or local variable). Allows preservation of computational
 * results in named variables.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */
public class StoreInstruction extends AbstractVarInstruction {
    public static final String OP_CODE = "store";

    public StoreInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE, varName);
    }

    @Override
    protected void doExecute(Frame frame) {
        int value = frame.pop();
        Variable var = frame.variable(varName);
        var.store(value);
    }
}
