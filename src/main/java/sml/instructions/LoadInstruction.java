package sml.instructions;

import sml.*;

/**
 * Represents the variable loading instruction.
 *
 * <p>The LoadInstruction class implements variable access operations that:</p>
 * <ul>
 *   <li>Locates a variable in the current execution context</li>
 *   <li>Retrieves its integer value</li>
 *   <li>Pushes the value onto the operand stack for subsequent operations</li>
 * </ul>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Provides access to stored variable values</li>
 *   <li>Transfers data from variable storage to operand stack</li>
 *   <li>Enables computation using previously stored values</li>
 * </ul>
 *
 * @author Ricki Angel
 */
public class LoadInstruction extends AbstractVarInstruction {
    public static final String OP_CODE = "load";

    /**
     * Constructs a new LoadInstruction with specified label and variable identifier.
     *
     * <p>Initializes the instruction with:</p>
     * <ul>
     *   <li>An optional source label for this instruction</li>
     *   <li>A required variable identifier to load from</li>
     * </ul>
     *
     * @param label   The label identifying this instruction (can be null)
     * @param varName The identifier of the variable to load
     */
    public LoadInstruction(Label label, Variable.Identifier varName) {
        super(label, OP_CODE, varName);
    }

    /**
     * Executes the instruction's primary operation.
     *
     * <p>Performs the variable loading by:</p>
     * <ul>
     *   <li>Retrieving the variable from the current frame</li>
     *   <li>Loading its integer value</li>
     *   <li>Pushing the value onto the operand stack</li>
     *   <li>Displaying the loaded value for debugging</li>
     * </ul>
     *
     * @param frame The current execution frame
     */
    @Override
    protected void doExecute(Frame frame) {
        Variable var = frame.variable(varName);
        int value = var.load();
        frame.push(value);
        System.out.println(value);
    }
}
