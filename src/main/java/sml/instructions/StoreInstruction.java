package sml.instructions;

import sml.*;

/**
 * Represents the variable storage instruction in the SML runtime environment.
 *
 * <p>The StoreInstruction class implements variable assignment operations that:</p>
 * <ul>
 *   <li>Pop a value from the operand stack</li>
 *   <li>Store it in a named variable within the current frame</li>
 *   <li>Enable data persistence across instruction executions</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Transfers values from the operand stack to variable storage</li>
 *   <li>Manages variable state changes during execution</li>
 *   <li>Provides named access to computational results</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public non-sealed class StoreInstruction extends AbstractVarInstruction {
    public static final String OP_CODE = "store";

    /**
     * Constructs a new StoreInstruction with specified label and variable identifier.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>A required variable identifier to store to</li>
     * </ul>
     *
     * @param label The label identifying this instruction (can be null)
     * @param varName The identifier of the variable to store into
     */
    public StoreInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE, varName);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the variable storage by:</p>
     * <ul>
     *   <li>Popping a value from the operand stack</li>
     *   <li>Locating the target variable in the current frame</li>
     *   <li>Storing the value in the variable</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        int value = frame.pop();
        Variable var = frame.variable(varName);
        var.store(value);
    }
}