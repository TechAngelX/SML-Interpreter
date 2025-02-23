package sml.instructions;

import sml.*;

/**
 * =====================================================================================
 * Represents the 'load' instruction from the Simple Machine Language.
 * -------------------------------------------------------------------------------------
 * <p>
 * Retrieves a value from a variable (either a method argument or local variable),
 * and pushes it onto the current operand stack.
 * <p>
 * The {@code doExecute} method defines the instruction's core operational logic.
 *
 * @author Ricki Angel
 */
public class LoadInstruction extends AbstractVarInstruction {
    public static final String OP_CODE = "load";

    public LoadInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE, varName);
    }

    @Override
    protected void doExecute(Frame frame) {
        Variable var = frame.variable(varName);
        int value = var.load();
        frame.push(value);
        System.out.println(value);
    }
}
